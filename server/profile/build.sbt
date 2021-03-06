import Dependencies._

Build.Settings.project

name := "lift-profile"

libraryDependencies ++= Seq(
  akka.actor,
  akka.cluster,
  akka.contrib,
  spray.routing,
  scalaz.core,
  akka.testkit % "test",
  spray.testkit % "test"
)
