package com.petwalk.utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val applicationConfig = config.getConfig("application")
  private val elasticsearchConfig = config.getConfig("elasticsearch")

  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")
  val publicDns = httpConfig.getString("publicDns")

  val elasticsearchUrl = elasticsearchConfig.getString("url")
  val gymsIndex = elasticsearchConfig.getString("walkers-index")

  val timezone = applicationConfig.getString("timezone")
}
