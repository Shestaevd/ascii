package shd.asciiwebcam.functions.console

import cats.effect.IO

object Console {

  def clearConsole: IO[Unit] = IO(
    if (System.getProperty("os.name").contains("Windows")) {
      val pb: ProcessBuilder = new ProcessBuilder("cmd", "/c", "cls")
      val startProcess: Process = pb.inheritIO.start
      startProcess.waitFor()
    } else {
      val pb: ProcessBuilder = new ProcessBuilder("clear")
      val startProcess: Process = pb.inheritIO.start
      startProcess.waitFor()
    }
  )

  def printImage(asciiList: List[List[Char]]): IO[Unit] = IO(
    asciiList.foreach { h =>
      h.foreach(w => print(w))
      print("\n")
    }
  )

}
