ThisBuild / scalaVersion := "3.3.5"
ThisBuild / scalacOptions ++= Seq(
  "-feature",
  "-Werror",
  "-deprecation",
  "-Wunused:all"
)

val dependencies =
  Dependencies.effects ++
    Dependencies.logging ++
    Dependencies.circe ++
    Dependencies.utils ++
    Dependencies.testing

lazy val `openquest-builders` = project
  .in(file("."))
  .settings(
    libraryDependencies ++= dependencies,
    Compile / run / fork := true
  )
