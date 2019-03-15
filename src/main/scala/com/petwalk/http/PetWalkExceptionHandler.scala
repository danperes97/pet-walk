package com.petwalk.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import StatusCodes._
import Directives._
import org.slf4j.LoggerFactory
import com.petwalk.exception._

trait PetWalkExceptionHandler {
  val logger = LoggerFactory.getLogger(classOf[PetWalkExceptionHandler])

  implicit def petWalkExceptionHandler: ExceptionHandler = {
    ExceptionHandler {
      case e: AuthorizationException => {
        extractUri { uri =>
          logger.debug(s"AuthorizationException to $uri")
          complete(Unauthorized -> e.getMessage)
        }
      }
      case e: BusinessException => {
        extractUri { uri =>
          logger.debug(s"BusinessException to $uri")
          complete(BadRequest -> e.getMessage)
        }
      }
      case e: Exception => {
        logger.error(e.getMessage, e)
        complete(InternalServerError)
      }
    }
  }
}
