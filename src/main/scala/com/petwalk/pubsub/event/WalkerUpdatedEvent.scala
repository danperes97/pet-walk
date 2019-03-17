package com.petwalk.pubsub.event

import cats.implicits._
import com.petwalk.model._

import com.petwalk.utils.CirceConfig._
import io.circe.generic.extras._

@ConfiguredJsonCodec
case class WalkerUpdatedEvent(
    token: String,
    name: String,
    coordinates: Coordinates,
    pets: List[Pet],
    walks: Long,
    likes: Long,
    phrase: String,
) {
  def toWalker: Walker = Walker(token, name, coordinates, pets, walks, likes, phrase)
}
