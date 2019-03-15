package com.petwalk.fixture

import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.ElasticClient
import com.petwalk.utils.Config
import com.petwalk.model._
import io.circe.generic.extras.auto._
import com.petwalk.utils.CirceConfig._
import com.sksamuel.elastic4s.circe._
import scala.io.Source

trait WalkerElasticsearchSupport extends Config with FixtureSupport {

  ElasticSearch.prepare

  import com.sksamuel.elastic4s.http.ElasticDsl._

  val localClient = ElasticClient(ElasticsearchClientUri(elasticsearchUrl))

  def index(walker: Walker) = {
    localClient.execute {
      bulk (
        indexInto(walkerIndex / "walker").doc(walker) id walker.token,
      ).immediateRefresh
    }.await
  }
}

object ElasticSearch extends Config {
  import com.sksamuel.elastic4s.http.ElasticDsl._

  val rawMapping = Source.fromResource("mappings/walkers_v01.json").mkString

  lazy val prepare = {
    val client = ElasticClient(ElasticsearchClientUri(elasticsearchUrl))
    deleteIndexes(client)
    createIndexes(client)
  }

  private def deleteIndexes(client: ElasticClient) =
    client.execute {
      deleteIndex(walkerIndex)
    }.await

  private def createIndexes(client: ElasticClient) =
    client.execute {
      createIndex(walkerIndex).source(rawMapping)
    }.await
}
