package shd.asciiwebcam

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.catsSyntaxApplicativeId
import org.bytedeco.javacv.OpenCVFrameGrabber
import org.jline.terminal.{Terminal, TerminalBuilder}
import shd.asciiwebcam.functions.config.Config
import shd.asciiwebcam.functions.console.Console.{clearConsole, printImage}
import shd.asciiwebcam.functions.image.Image.{compressTo, toAsciiSequence}
import shd.asciiwebcam.functions.rich.Rich.RichInt
import shd.asciiwebcam.functions.webcam.Webcam.{webcamGrabber, webcamImage}

object Launcher extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      config <- Config.read
      terminal <- TerminalBuilder.terminal().pure[IO]
      grabber <- webcamGrabber(config.device)
      _ <- tick(config, terminal, grabber)
    } yield ExitCode.Success

  def tick(config: Config, terminal: Terminal, grabber: OpenCVFrameGrabber): IO[Unit] =
    (for {
      image <- webcamImage(grabber)
      resizedImage <- compressTo(terminal.getWidth.ifZero(config.defaultWidth), terminal.getHeight.ifZero(config.defaultHeight), image)
      asciiList <- toAsciiSequence(resizedImage, config)
      _ <- clearConsole
    } yield printImage(terminal, asciiList)) >> tick(config, terminal, grabber)

}
