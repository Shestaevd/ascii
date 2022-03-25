ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(name := "ImagePlayground")
  .settings(libraryDependencies ++=
    Seq(
      "jline" % "jline" % "3.0.0.M1",
      "org.imgscalr" % "imgscalr-lib" % "4.2",
      "com.github.pureconfig" %% "pureconfig" % "0.17.1",
      "org.typelevel" %% "cats-effect" % "3.3.8"
    )
  )

assembly / mainClass := Some("shd.asciiweb.Launcher")
assembly / assemblyJarName := "ascii-web.jar"
