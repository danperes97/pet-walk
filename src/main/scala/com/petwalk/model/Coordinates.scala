package com.petwalk.model

import com.petwalk.utils.CirceConfig._
import io.circe.generic.extras._

@ConfiguredJsonCodec
case class Coordinates(lat: Double, lon: Double)
