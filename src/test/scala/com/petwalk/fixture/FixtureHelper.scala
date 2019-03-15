package com.petwalk.fixture

import br.com.six2six.fixturefactory.function.AtomicFunction
import scala.collection.JavaConverters._
import scala.reflect.ClassTag
import br.com.six2six.fixturefactory.Fixture

object FixtureHelper {

  def lazyVal[T](fun: => T): AtomicFunction = new AtomicFunction {
    override def generateValue[A](): A = fun.asInstanceOf[A]
  }

  def optionVal[T](f: { def generateValue[A](): A }): AtomicFunction = lazyVal(Option(f.generateValue()))

  def scalaList[T](total: Int, name: String)(implicit tag: ClassTag[T]): AtomicFunction = new AtomicFunction {
    override def generateValue[A](): A = {
      val javaList = Fixture.from(tag.runtimeClass).gimme(total, name).asInstanceOf[java.util.List[T]]
      javaList.asScala.toList.asInstanceOf[A]
    }
  }

  def scalaListEmpty[T] = new java.util.ArrayList[T]().asScala.toList
}
