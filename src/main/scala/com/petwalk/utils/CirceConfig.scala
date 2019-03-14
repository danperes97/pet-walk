package com.petwalk.utils

import io.circe.generic.extras._

object CirceConfig {
  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames
}
