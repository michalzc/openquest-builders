ThisBuild / scalaVersion := "3.3.5"
ThisBuild / scalacOptions ++= Seq(
  "-feature",
  "-Werror"
)

val dependencies = Seq(
  "org.scala-lang.modules"     %% "scala-parser-combinators" % "2.3.0",
  "org.typelevel"              %% "cats-core"                % "2.13.0",
  "com.lihaoyi"                %% "os-lib"                   % "0.11.4",
  "ch.qos.logback"              % "logback-classic"          % "1.5.17",
  "com.typesafe.scala-logging" %% "scala-logging"            % "3.9.5",
  "io.circe"                   %% "circe-yaml"               % "0.16.0",
  "io.circe"                   %% "circe-parser"             % "0.14.10",
  "io.circe"                   %% "circe-generic"            % "0.14.10",
  "com.github.slugify"          % "slugify"                  % "3.0.7",
  "org.scalatest"              %% "scalatest"                % "3.2.19" % Test,
  "org.specs2"                 %% "specs2-core"              % "5.5.8"  % Test
)

lazy val `openquest-builders` = project.in(file(".")).settings(libraryDependencies ++= dependencies)
