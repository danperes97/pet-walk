package com.petwalk.http.api

import com.petwalk.model._
import akka.http.scaladsl.server._
import Directives._
import akka.http.scaladsl.server.Directives._
import com.petwalk.service._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings

trait PetWalkApi extends BaseApi with WalkerSearchService {

  val settings = CorsSettings.defaultSettings.withAllowCredentials(true)

  def nearby =
    cors(settings) {
      pathPrefix("nearby") {
        get {
          pathEndOrSingleSlash {
            parameters('coordinates.as[Coordinates], 'excludeTokens.?) { (coordinates, tokens) =>
              complete(walkerSearchService.searchNearby(coordinates, tokens.map(_.split(",")).fold(Seq[String]())(_.toSeq)))
            }
          }
        }
      }
    }

  def walker =
    cors(settings) {
      pathPrefix("walker") {
        get {
          pathEndOrSingleSlash {
            parameters('token.as[String]) { (token) =>
              complete(walkerSearchService.searchWalker(token))
            }
          }
        }
      }
    }


  val petWalkRoutes = pathPrefix("walkers") { nearby ~ walker }
}
