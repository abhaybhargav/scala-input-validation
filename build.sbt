import sbt.Keys._
import sbt._

name := "scala-validation-demo"
version := "0.1"
scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.2.9",
  "com.typesafe.akka" %% "akka-stream" % "2.6.19",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.19",
  "de.heikoseeberger" %% "akka-http-json4s" % "1.39.2",
  "org.json4s" %% "json4s-jackson" % "4.0.5",
  "com.github.andyglow" %% "scala-jsonschema" % "0.7.5",
  "ch.megard" %% "akka-http-cors" % "1.1.3"
)

// Assembly configuration for creating a fat JAR
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "application.conf"            => MergeStrategy.concat
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}

enablePlugins(AssemblyPlugin)