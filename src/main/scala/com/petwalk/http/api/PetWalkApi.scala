package com.petwalk.http.api

import com.petwalk.model._
import akka.http.scaladsl.server._
import Directives._
import akka.http.scaladsl.server.Directives._
import com.petwalk.service._


trait PetWalkApi extends BaseApi with WalkerSearchService {

  def nearby =
    pathPrefix("nearby") {
      get {
        pathEndOrSingleSlash {
          parameters('coordinates.as[Coordinates]) { (coordinates) =>
            complete(walkerSearchService.searchNearby(coordinates))
          }
        }
      }
    }

  def walker =
    pathPrefix("walker") {
      get {
        pathEndOrSingleSlash {
          parameters('token.as[String]) { (token) =>
            complete(walkerSearchService.searchWalker(token))
          }
        }
      }
    }


  val petWalkRoutes = pathPrefix("walkers") { nearby ~ walker }
}
