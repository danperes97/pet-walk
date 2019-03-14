package com.petwalk.http.api

import com.petwalk.model._
import akka.http.scaladsl.server._
import Directives._
import akka.http.scaladsl.server.Directives._

trait PetWalkApi extends BaseApi {

  def nearby =
    pathPrefix("nearby") {
      get {
        pathEndOrSingleSlash {
          parameters('coordinates.as[Coordinates]) { (coordinates) =>
            // complete(petWalkService.searchNearby(coordinates))
            complete("Funfou")
          }
        }
      }
    }

  val petWalkRoutes = pathPrefix("walkers") { nearby }
}
