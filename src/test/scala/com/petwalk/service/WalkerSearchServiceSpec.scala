package com.petwalk.service.remote.elasticsearch

import scala.concurrent.duration._
import scala.concurrent.Await
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.concurrent.ScalaFutures

import com.petwalk.model.Walker
import com.petwalk.fixture.{ FixtureSupport, WalkerElasticsearchSupport }

class SearchSpec extends WordSpec with Matchers
    with ScalaFutures with FixtureSupport
    with Search with WalkerElasticsearchSupport {
  implicit val timeout: Duration = 20 seconds

  "find walker" should {
    "search by coordinates" in {
      val walkerFixture = buildFixture[Walker]("default"); index(walkerFixture)

      val response = searchElasticsearch.searchGeolocation(walkerFixture.coordinates)
      Await.result(response, timeout)

      response.futureValue.map(_.walker.name) should contain(walkerFixture.name)
    }

    "search by token" in {
      val walkerFixture = buildFixture[Walker]("default"); index(walkerFixture)

      val response = searchElasticsearch.searchToken(walkerFixture.token)
      Await.result(response, timeout)

      response.futureValue.map(_.name) should contain(walkerFixture.name)
    }
  }
}
