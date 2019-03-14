package com.petwalk

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.event.{ Logging, LoggingAdapter }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import com.petwalk.http.Routes
import com.petwalk.utils.Config

object Main extends App with Config with Routes {
  import com.petwalk.utils.AkkaExecutor.Global._
  val log: LoggingAdapter = Logging(system, getClass)

  Http().bindAndHandleAsync(Route.asyncHandler(routes), httpInterface, httpPort)

  scala.sys.addShutdownHook {
    log.info("Terminating...")
    system.terminate()
    Await.result(system.whenTerminated, 30 seconds)
    log.info("Terminated... Bye")
  }
}
