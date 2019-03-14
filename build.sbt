lazy val akkaHttpVersion = "10.1.3"
lazy val akkaVersion     = "2.5.14"
lazy val circeVersion    = "0.9.3"
lazy val opRabbitVersion = "2.1.0"
lazy val rollbarVersion = "1.3.1"
lazy val elastic4sVersion = "6.3.8"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.pet-walk",
      scalaVersion    := "2.12.4"
    )),
    name := "pet-walk",
    libraryDependencies ++= Seq(
      "ch.qos.logback"     % "logback-classic" % "1.2.3",
      "org.typelevel"     %% "cats-core"       % "1.3.0",
      "com.typesafe.akka" %% "akka-http"       % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"     % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
      "de.heikoseeberger" %% "akka-http-circe" % "1.21.0",
      "io.circe"          %% "circe-generic-extras" % circeVersion,
      "io.circe"          %% "circe-generic"        % circeVersion,
      "io.circe"          %% "circe-java8"          % circeVersion,
      "io.circe"          %% "circe-parser"         % circeVersion,
      "com.spingo"        %% "op-rabbit-core"        % opRabbitVersion,
      "com.spingo"        %% "op-rabbit-circe"       % opRabbitVersion,
      "com.spingo"        %% "op-rabbit-akka-stream" % opRabbitVersion,

      "com.rollbar" % "rollbar-logback" % rollbarVersion,

      "com.sksamuel.elastic4s" %% "elastic4s-core"  % elastic4sVersion,
      "com.sksamuel.elastic4s" %% "elastic4s-http"  % elastic4sVersion,
      "com.sksamuel.elastic4s" %% "elastic4s-circe" % elastic4sVersion,

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "org.scalatest"     %% "scalatest"         % "3.0.5"         % Test,
      "br.com.six2six"     % "fixture-factory"   % "3.1.0"         % Test,
      "org.apache.commons" % "commons-math"      % "2.2"           % Test,
      "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0"   % Test
    )
  )

  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

enablePlugins(JavaAgent)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
import com.typesafe.sbt.packager.docker._
dockerExposedPorts := Seq(9000)
dockerUsername := Some("Peter Walker Son")
dockerUpdateLatest := true
dockerEntrypoint := Seq("/bin/sh", "-c")
bashScriptExtraDefines += """addJava "-Dconfig.resource=application-prod.conf""""
bashScriptExtraDefines += """addJava "-Dlogback.configurationFile=logback-prod.xml""""
dockerCommands ++= Seq(
  Cmd("USER", "root")
)
