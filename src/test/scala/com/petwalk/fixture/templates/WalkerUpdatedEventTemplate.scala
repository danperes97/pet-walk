package com.petwalk.fixture.templates

import br.com.six2six.fixturefactory.{ Fixture, Rule }
import br.com.six2six.fixturefactory.loader.TemplateLoader
import br.com.six2six.fixturefactory.function.AtomicFunction

import com.petwalk.model._
import com.petwalk.pubsub.event._
import com.petwalk.fixture.FixtureHelper._

class WalkerUpdatedEventTemplate extends TemplateLoader {

  override def load() {
    Fixture.of(classOf[WalkerUpdatedEvent]).addTemplate("default", new Rule {
      add("token", regex("\\w{10}"))
      add("name", regex("\\w{10}"))
      add("phrase", regex("\\w{10}"))
      add("coordinates", one(classOf[Coordinates], "default"))
      add("pets", scalaList[Pet](4, "default"))
      add("walks", 250.toLong)
      add("likes", 250.toLong)
    })
  }
}
