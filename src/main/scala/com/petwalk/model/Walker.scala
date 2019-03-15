package com.petwalk.model

import com.petwalk.utils.CirceConfig._
import io.circe.generic.extras._

@ConfiguredJsonCodec
case class Walker(token: String, name: String, coordinates: Coordinates, pets: List[Pet])

@ConfiguredJsonCodec
case class NearWalker(walker: Walker, distance: BigDecimal)
