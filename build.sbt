val circeVersion = "0.9.3"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.3",
  version := "0.1"
)

lazy val circeeDependencies = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

val catsDependencies = Seq(
  "org.typelevel" %% "cats-core" % "1.0.1",
  "org.typelevel" %% "cats-effect" % "1.0.0-RC",
)

val fs2Dependencies = Seq(
  "co.fs2" %% "fs2-core" % "0.10.4",
  "co.fs2" %% "fs2-io" % "0.10.4"
)

val http4sDependencies = Seq(
  "org.http4s" % "http4s-core_2.12" % "0.18.11"
)


val doobieVersion = "0.5.3"
val doobieDependencies = Seq(
  "org.tpolecat" %% "doobie-core"      % doobieVersion,
  "org.tpolecat" %% "doobie-h2"        % doobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % doobieVersion
)

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

lazy val root = (project in file("."))
  .enablePlugins()
  .settings(
    commonSettings,
    name := "typelevel-ecosystem",
    libraryDependencies ++= catsDependencies ++ circeeDependencies ++ fs2Dependencies ++ doobieDependencies ++ testDependencies,
    scalacOptions+= "-Ypartial-unification"
  )