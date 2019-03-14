package com.petwalk.utils

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext

object AkkaExecutor {
  object Global {
    implicit val system = ActorSystem()
    implicit val executor: ExecutionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()
  }
}
