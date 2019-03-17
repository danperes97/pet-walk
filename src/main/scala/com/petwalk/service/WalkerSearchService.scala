package com.petwalk.service

import scala.concurrent.Future
import com.petwalk.service.remote.elasticsearch.{ Search }
import com.petwalk.model._

import com.petwalk.utils.Config

trait WalkerSearchService extends Config with Search {
  import com.petwalk.utils.AkkaExecutor.Global._

  val walkerSearchService = this

  def searchNearby(coordinates: Coordinates, tokens: Seq[String]): Future[Seq[NearWalker]] =
    searchElasticsearch.searchGeolocation(coordinates, tokens)

  def searchWalker(token: String): Future[Seq[Walker]] =
    searchElasticsearch.searchToken(token)
}
