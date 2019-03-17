package com.petwalk.fixture.templates

import br.com.six2six.fixturefactory.function.AtomicFunction
import br.com.six2six.fixturefactory.{ Fixture, Rule }
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.petwalk.fixture.FixtureHelper._

import com.petwalk.model._

class WalkerTemplate extends TemplateLoader {

  override def load() {
    Fixture.of(classOf[Walker]).addTemplate("default", new Rule {
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
