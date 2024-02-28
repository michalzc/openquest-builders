ThisBuild / scalaVersion := "3.3.1"

val dependencies = Seq(
  "org.scala-lang.modules"     %% "scala-parser-combinators" % "2.3.0",
  "org.typelevel"              %% "cats-core"                % "2.10.0",
  "com.lihaoyi"                %% "os-lib"                   % "0.9.3",
  "ch.qos.logback"              % "logback-classic"          % "1.5.0",
  "com.typesafe.scala-logging" %% "scala-logging"            % "3.9.5",
  "org.scalatest"              %% "scalatest"                % "3.2.18" % Test
)

lazy val `openquest-builders` = project
  .in(file("."))
  .settings(
    libraryDependencies ++= dependencies
  )
