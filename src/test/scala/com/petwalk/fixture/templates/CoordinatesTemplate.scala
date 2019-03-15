package com.petwalk.fixture.templates

import br.com.six2six.fixturefactory.function.AtomicFunction
import br.com.six2six.fixturefactory.{ Fixture, Rule }
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.petwalk.fixture.FixtureHelper._

import com.petwalk.model._

class CoordinatesTemplate extends TemplateLoader {

  override def load() {
    Fixture.of(classOf[Coordinates]).addTemplate("default", new Rule {
      add("lat", 51.5092067)
      add("lon", -0.0790898)
    })
  }
}
