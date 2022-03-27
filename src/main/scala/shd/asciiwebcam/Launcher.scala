package shd.asciiwebcam

import cats.effect._
import cats.implicits._
import org.bytedeco.javacv.OpenCVFrameGrabber
import org.jline.terminal._
import shd.asciiwebcam.functions.config.Config
import shd.asciiwebcam.functions.console.Console._
import shd.asciiwebcam.functions.image.Image._
import shd.asciiwebcam.functions.rich.Rich.RichInt
import shd.asciiwebcam.functions.webcam.Webcam._

object Launcher extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      config <- Config.read
      terminal <- TerminalBuilder.terminal().pure[IO]

      _ <- grabber(config.device).use(tick(config, terminal, _).foreverM)
    } yield ExitCode.Success

  def tick(
      config: Config,
      terminal: Terminal,
      grabber: OpenCVFrameGrabber
  ): IO[Unit] =
    for {
      image <- webcamImage(grabber)

      resizedImage <- compressTo(
        terminal.getWidth.ifZero(config.defaultWidth),
        terminal.getHeight.ifZero(config.defaultHeight),
        image
      )

      asciiList <- toAsciiSequence(resizedImage, config)

      _ <- printImage(terminal, asciiList)
      _ <- clearConsole
    } yield ()

}
