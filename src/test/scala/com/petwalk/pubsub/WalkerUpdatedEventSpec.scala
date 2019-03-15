package com.petwalk.pubsub

import akka.actor._
import scala.concurrent.Await
import scala.concurrent.duration._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.stream._
import com.spingo.op_rabbit.CirceSupport._
import com.timcharper.acked.AckedSource
import io.circe.syntax._
import com.petwalk.fixture.FixtureSupport
import com.petwalk.pubsub.event.WalkerUpdatedEvent

class WalkerUpdatedEventSpec extends WordSpec with Matchers
    with WalkerUpdatedSub with ScalaFutures with FixtureSupport {

  import com.petwalk.utils.AkkaExecutor.Global._
  implicit val timeout: Duration = 20 seconds

  "publish new walker updated event message" should {
    def fixture = new {
      val data = (0 to 5).map { _ => buildFixture[WalkerUpdatedEvent]("default") }
    }

    "have snake case" in {
      fixture.data.asJson.findAllByKey("name") should not be empty
    }

    "consume the message with resource.walker.updated routing key" in {
      WalkerUpdatedSub.up

      val rabbitControl = system.actorOf(Props(new RabbitControl))
      val published = AckedSource(fixture.data)
        .map(Message(_, Publisher.exchange(ExchangeName, RoutingKey)))
        .runWith(MessagePublisherSink(rabbitControl))

      Await.result(published, timeout)

      whenReady(published) { s => s should be(()) }
    }
  }
}
