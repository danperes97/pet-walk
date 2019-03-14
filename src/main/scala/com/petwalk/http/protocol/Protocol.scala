package com.petwalk.http.protocol

import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import akka.http.scaladsl.model.MediaTypes.`application/json`
import scala.concurrent.ExecutionContext

import akka.http.scaladsl.model._
import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.unmarshalling._
import akka.http.scaladsl.model.headers.Language

import com.petwalk.model.Coordinates

trait Protocol extends FailFastCirceSupport {
  import MediaVersionTypes._

  override val mediaTypes =
    List(`application/json`, `application/vnd.petwalk.v1+json`)

  override def unmarshallerContentTypes =
    List(`application/json`, `application/vnd.petwalk.v1+json`)

  implicit def unitMarshaller(implicit ec: ExecutionContext): ToEntityMarshaller[Unit] = Marshaller.oneOf(
    Marshaller.withFixedContentType(`application/vnd.petwalk.v1+json`) { result =>
      val noBody: Array[Byte] = Array()
      HttpEntity(ContentType(`application/vnd.petwalk.v1+json`), noBody)
    }
  )

  val coordinatesPattern =  """^([+-]?\d+\.?\d+)\s*,\s*([+-]?\d+\.?\d+)$""".r
  implicit def CoordinatesUnmarshall: Unmarshaller[String, Coordinates] =
    Unmarshaller.strict[String, Coordinates] { string ⇒
      string match {
        case coordinatesPattern(lat, lon) => Coordinates(lat.toDouble, lon.toDouble)
        case _ => throw new IllegalArgumentException("invalid coordinates. eg. 40.72451,-73.94945")
      }
    }

  val defaultLanguage = Language("en")
  private val acceptedLanguages =
    Seq(
      "pt",
      "en",
      "es",
      "de",
      "en-GB",
      "en-IE",
      "es-AR",
      "es-CL",
      "es-MX",
      "es-UY",
      "fr",
      "it",
      "nl",
      "pt-PT")

  val acceptLanguages = acceptedLanguages.map(Language(_))
}

object MediaVersionTypes {
  def customMediatype(subType: String) = MediaType.customWithFixedCharset("application", subType, HttpCharsets.`UTF-8`)

  val `application/vnd.petwalk.v1+json` = customMediatype("vnd.petwalk.v1+json")
}
