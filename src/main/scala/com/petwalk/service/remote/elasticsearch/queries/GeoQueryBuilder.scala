package com.petwalk.service.remote.elasticsearch.queries

import com.petwalk.model._

import com.sksamuel.elastic4s._
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.searches.GeoPoint
import com.sksamuel.elastic4s.searches.sort.Sort
import com.sksamuel.elastic4s.searches.queries.Query
import com.sksamuel.elastic4s.searches.sort.SortOrder.ASC

object GeoQueryBuilder {
  def distanceQuery(coordinates: Coordinates, distance: Double): Query =
    geoDistanceQuery("coordinates").point(coordinates.lat, coordinates.lon) distance (distance, DistanceUnit.Kilometers)

  def sortQuery(coordinates: Coordinates): Sort =
    geoSort("coordinates") points (List(GeoPoint(coordinates.lat, coordinates.lon))) unit(DistanceUnit.Kilometers) order ASC

}
