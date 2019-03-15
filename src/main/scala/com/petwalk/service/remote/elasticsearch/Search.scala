package com.petwalk.service.remote.elasticsearch

import scala.concurrent.Future

import com.petwalk.utils.AkkaExecutor.Global._
import com.petwalk.service.remote.elasticsearch.queries._
import com.petwalk.model._

import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.search.SearchResponse
import com.sksamuel.elastic4s.searches.queries.{ Query, BoolQuery }
import com.sksamuel.elastic4s.http.{ RequestFailure, RequestSuccess, Response }

trait Search extends Elasticsearch {
  protected val searchElasticsearch = this
  private val DefaultDistance = 1.0

  def searchGeolocation(coordinates: Coordinates): Future[Seq[NearWalker]] =
    walkerElasticsearch.client.execute {
      search(walkerIndex / "walker").query {
        must(GeoQueryBuilder.distanceQuery(coordinates, DefaultDistance))
      } sortBy { GeoQueryBuilder.sortQuery(coordinates) }
    }.map(nearWalkers => processResult(nearWalkers))

  private def processResult(searchResponse: Response[_]): Seq[NearWalker] =
    searchResponse match {
      case failure: RequestFailure => walkerElasticsearch.error(s"fetch result ${failure.error.reason}")
      case results: RequestSuccess[SearchResponse] => buildNearWalkers(results.result)
    }
}
