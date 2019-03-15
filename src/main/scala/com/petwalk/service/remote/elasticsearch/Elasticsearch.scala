package com.petwalk.service.remote.elasticsearch

import scala.concurrent.Future
import org.slf4j.LoggerFactory

import com.petwalk.utils.Config
import com.petwalk.utils.AkkaExecutor.Global._
import com.petwalk.model._
import com.petwalk.exception.{ ElasticSearchException, InvalidMessageException }

import io.circe.jawn._

import org.apache.http.client.config.RequestConfig

import com.sksamuel.elastic4s._
import com.sksamuel.elastic4s.circe._
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.ElasticClient
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.search.SearchResponse
import com.sksamuel.elastic4s.http.ElasticDsl.{ update => elasticUpdate }
import com.sksamuel.elastic4s.http.{ RequestFailure, RequestSuccess }
import com.sksamuel.elastic4s.http.{ ElasticClient, ElasticProperties }

trait Elasticsearch extends Config {
  val walkerElasticsearch = this

  private val TimeoutInMillis = 10000
  private val logger = LoggerFactory.getLogger(classOf[Elasticsearch])

  val client = ElasticClient(ElasticsearchClientUri(elasticsearchUrl))

  def save(walker: Walker): Future[Unit] =
    client.execute {
      elasticUpdate(walker.token).in(walkerIndex / "walker").docAsUpsert(walker)
    }.flatMap {
      case failure @ RequestFailure(status, _, _, _) =>
        if (status >= 500) Future.failed(ElasticSearchException(s"Update error: ${failure.error}"))
        else Future.failed(InvalidMessageException(s"Update error: ${failure.error}"))
      case _: RequestSuccess[_] =>
        Future.successful(())
    }

  def buildNearWalkers(response: SearchResponse): Seq[NearWalker] = {
    response.hits.hits.map { hit =>
      decode[Walker](hit.sourceAsString).fold(
        (err) => {
          logger.warn(s"decode error: ${hit.sourceAsString}", err)
          None
        },
        (walker) => Some(NearWalker(walker, BigDecimal(hit.sort.head.head.toString)))
      )
    }.toSeq.flatten
  }

  def error(message: String, cause: Option[Throwable] = None) =
    throw new ElasticSearchException(s"Elastic Search: $message", cause)
}

private case class ElasticResponse(hits: ElasticWalkers)
private case class ElasticWalkers(hits: Array[ElasticWalker])
private case class ElasticWalker(_source: Walker, sort: Array[BigDecimal])
