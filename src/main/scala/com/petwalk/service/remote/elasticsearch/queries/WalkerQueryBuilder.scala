package com.petwalk.service.remote.elasticsearch.queries

import com.petwalk.model._

import com.sksamuel.elastic4s._
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.searches.queries.Query


object WalkerQueryBuilder {
  def tokenQuery(token: String): Query = termQuery("token", token)
}
