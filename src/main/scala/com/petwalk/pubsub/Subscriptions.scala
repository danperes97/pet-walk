package com.petwalk.pubsub

trait Subscriptions {
  val subscriptions = this

  def upSubs() {
    WalkerUpdatedSub.up
  }
}
