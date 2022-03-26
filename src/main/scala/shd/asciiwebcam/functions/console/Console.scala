package shd.asciiwebcam.functions.console

import cats.effect.IO
import org.jline.terminal.Terminal

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

  def printImage(terminal: Terminal, asciiList: List[List[Char]]): Unit = {
    val writer = terminal.writer()
    asciiList.foreach { h =>
      h.foreach(w => writer.print(w))
      writer.print("\n")
    }
    writer.flush()
  }


}
