ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(name := "ImagePlayground")
  .settings(
    libraryDependencies ++=
      Seq(
        "org.jline" % "jline" % "3.21.0",
        "org.imgscalr" % "imgscalr-lib" % "4.2",
        "com.github.pureconfig" %% "pureconfig" % "0.17.1",
        "org.typelevel" %% "cats-effect" % "3.3.8"
      )
  )

assembly / mainClass := Some("shd.asciiwebcam.Launcher")
assembly / assemblyJarName := "ascii-web.jar"
