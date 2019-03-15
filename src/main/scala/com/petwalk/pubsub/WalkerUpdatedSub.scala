package com.petwalk.pubsub

import scala.concurrent.duration._
import akka.actor._
import com.timcharper.acked._
import com.rabbitmq.client.Channel
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.stream._
import com.spingo.op_rabbit.CirceSupport._
import com.petwalk.exception.InvalidMessageException
import com.petwalk.service.remote.elasticsearch.Elasticsearch
import com.petwalk.pubsub.event.WalkerUpdatedEvent

trait WalkerUpdatedSub extends Elasticsearch {
  import Directives._
  import com.petwalk.utils.AkkaExecutor.Global._
  import akka.stream.ActorAttributes.supervisionStrategy
  import akka.stream.Supervision.resumingDecider

  val ExchangeName = "event.topic.resource"
  val QueueName = "walkers_updated"
  val RoutingKey = "resource.walker.updated"
  val qos = 8

  implicit val recoveryStrategy = new RecoveryStrategy {
    val errorQueue = RecoveryStrategy.abandonedQueue(defaultTTL=365.days, abandonQueueName=(_: String) => s"${QueueName}-error")
    val invalidQueue = RecoveryStrategy.abandonedQueue(defaultTTL=365.days, abandonQueueName=(_: String) => s"${QueueName}-invalid")
    val redeliverStrategy = RecoveryStrategy.limitedRedeliver(onAbandon=errorQueue, retryQueueName=(_: String) => s"${QueueName}-retry")

    def apply(queueName: String, channel: Channel, ex: Throwable): Handler = ex match {
      case _: InvalidMessageException | _: InvalidFormat => invalidQueue(queueName, channel, ex)
      case _ => redeliverStrategy(queueName, channel, ex)
    }
  }

  def up {
    val rabbitControl = system.actorOf(Props(new RabbitControl))

    val source = RabbitSource(
      rabbitControl,
      channel(qos),
      consume(Binding.topic(Queue(QueueName, autoDelete = false), Seq(RoutingKey), Exchange.topic(ExchangeName, autoDelete = false))),
      body(as[WalkerUpdatedEvent]))

    val flow = AckedFlow[WalkerUpdatedEvent]
      .mapAsync(qos)(walkerUpdatedEvent => walkerElasticsearch.save(walkerUpdatedEvent.toWalker))
      .withAttributes(supervisionStrategy(resumingDecider))

    val sink = AckedSink.ack

    source.via(flow).to(sink).run
  }
}

object WalkerUpdatedSub extends WalkerUpdatedSub
