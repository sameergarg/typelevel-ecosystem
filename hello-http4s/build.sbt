val circeVersion = "0.9.3"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.3",
  version := "0.1"
)

lazy val circeeDep = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

val catsDep = Seq(
  "org.typelevel" %% "cats-core" % "1.0.1",
  "org.typelevel" %% "cats-effect" % "1.0.0-RC",
  "org.typelevel" %% "cats-mtl-core" % "0.2.1"
)

val fs2Dep = Seq(
  "co.fs2" %% "fs2-core" % "0.10.4",
  "co.fs2" %% "fs2-io" % "0.10.4"
)

val http4sVersion = "0.18.11"
val http4sDep = Seq(
  "org.http4s" %% "http4s-dsl",
  "org.http4s" %% "http4s-blaze-server",
  "org.http4s" %% "http4s-blaze-client"
).map(_  % http4sVersion)

val http4sCirceeDep = Seq(
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % "0.9.3",
  "io.circe" %% "circe-literal" % "0.9.3"
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

val loggingDependencies = Seq(
  "ch.qos.logback"              % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
)

lazy val root = (project in file("."))
  .enablePlugins()
  .settings(
    commonSettings,
    name := "typelevel-ecosystem",
    libraryDependencies ++=
      loggingDependencies ++
      catsDep ++
      circeeDep ++
      fs2Dep ++
      doobieDependencies ++
      http4sDep ++
      http4sCirceeDep ++
      testDependencies,
    scalacOptions+= "-Ypartial-unification",
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )