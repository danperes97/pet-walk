package com.petwalk.exception

case class InvalidMessageException(message: String, cause: Option[Throwable] = None) extends RuntimeException(message, cause.orNull)

case class ElasticSearchException(message: String, cause: Option[Throwable] = None) extends RuntimeException(message, cause.orNull)
