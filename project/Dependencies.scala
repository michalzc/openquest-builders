import sbt.librarymanagement.ModuleID
import sbt.librarymanagement.syntax.*

object Dependencies {

  val effects: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core"            % Versions.catsCore,
    "org.typelevel" %% "cats-effect"          % Versions.catsEffect,
    "co.fs2"        %% "fs2-core"             % Versions.fs2,
    "co.fs2"        %% "fs2-io"               % Versions.fs2,
    "org.gnieh"     %% "fs2-data-csv"         % Versions.fs2Csv,
    "org.gnieh"     %% "fs2-data-csv-generic" % Versions.fs2Csv
  )

  val logging: Seq[ModuleID] = Seq(
    "org.typelevel"           %% "log4cats-core"    % Versions.catsLogging,
    "org.typelevel"           %% "log4cats-slf4j"   % Versions.catsLogging,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % Versions.log2j
  )

  val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core"    % Versions.circe,
    "io.circe" %% "circe-parser"  % Versions.circe,
    "io.circe" %% "circe-generic" % Versions.circe,
    "io.circe" %% "circe-yaml"    % Versions.circeYaml
  )

  val utils: Seq[ModuleID] = Seq(
    "com.github.slugify"      % "slugify"                  % Versions.slugify,
    "com.lihaoyi"            %% "os-lib"                   % Versions.osLib,
    "org.scala-lang.modules" %% "scala-parser-combinators" % Versions.parserCombinators
  )

  val testing: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-effect-testing-scalatest" % Versions.catsEffectScalaTest % Test,
    "org.scalatest" %% "scalatest"                     % Versions.scalaTest           % Test,
    "org.specs2"    %% "specs2-core"                   % Versions.specs2              % Test
  )
}
