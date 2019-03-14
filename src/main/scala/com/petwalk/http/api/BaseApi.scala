package com.petwalk.http.api

import akka.http.scaladsl.server._
import Directives._
import com.petwalk.utils.Config
import com.petwalk.http.protocol.Protocol

trait BaseApi extends Protocol with Config {
  val healthRoute =
    path("health") {
      get {
        complete("ok")
      }
    }
}
