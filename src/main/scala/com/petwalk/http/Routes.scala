package com.petwalk.http

import akka.http.scaladsl.server.Directives._
import com.petwalk.http.api.BaseApi
import com.petwalk.http.api.PetWalkApi

trait Routes extends PetWalkApi with PetWalkExceptionHandler with BaseApi {

  val routes = healthRoute ~ petWalkRoutes
}
