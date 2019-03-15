package com.petwalk.fixture

import br.com.six2six.fixturefactory.function.AtomicFunction
import br.com.six2six.fixturefactory.{ Fixture, Rule }
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.petwalk.fixture.FixtureHelper._

import com.petwalk.model._

class PetTemplate extends TemplateLoader {

  override def load() {
    Fixture.of(classOf[Pet]).addTemplate("default", new Rule {
      add("breed", random("Pitbull", "Chuaua"))
      add("name", regex("\\w{10}"))
      add("owner", regex("\\w{10}"))
    })
  }
}
