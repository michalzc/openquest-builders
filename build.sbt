ThisBuild / scalaVersion := "3.3.5"
ThisBuild / scalacOptions ++= Seq(
  "-feature",
  "-Werror"
)

val dependencies = Seq(
  "org.scala-lang.modules"  %% "scala-parser-combinators"      % "2.4.0",
  "org.typelevel"           %% "cats-core"                     % "2.13.0",
  "org.typelevel"           %% "cats-effect"                   % "3.5.7",
  "co.fs2"                  %% "fs2-core"                      % "3.11.0",
  "co.fs2"                  %% "fs2-io"                        % "3.11.0",
  "org.gnieh"               %% "fs2-data-csv"                  % "1.11.2",
  "org.gnieh"               %% "fs2-data-csv-generic"          % "1.11.2",
  "org.typelevel"           %% "log4cats-core"                 % "2.7.0",
  "org.typelevel"           %% "log4cats-slf4j"                % "2.7.0",
  "org.apache.logging.log4j" % "log4j-slf4j-impl"              % "2.24.3",
  "com.lihaoyi"             %% "os-lib"                        % "0.11.4",
  "io.circe"                %% "circe-core"                    % "0.14.10",
  "io.circe"                %% "circe-parser"                  % "0.14.10",
  "io.circe"                %% "circe-generic"                 % "0.14.10",
  "io.circe"                %% "circe-yaml"                    % "1.15.0",
  "com.github.slugify"       % "slugify"                       % "3.0.7",
  "com.github.tototoshi"    %% "scala-csv"                     % "2.0.0",
  "org.typelevel"           %% "cats-effect-testing-specs2"    % "1.6.0"  % Test,
  "org.typelevel"           %% "cats-effect-testing-scalatest" % "1.6.0"  % Test,
  "org.scalatest"           %% "scalatest"                     % "3.2.19" % Test,
  "org.specs2"              %% "specs2-core"                   % "5.5.8"  % Test
)

lazy val `openquest-builders` = project
  .in(file("."))
  .settings(
    libraryDependencies ++= dependencies,
    Compile / run / fork := true
  )
