package shd.asciiwebcam

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.catsSyntaxApplicativeId
import org.jline.terminal.TerminalBuilder
import shd.asciiwebcam.functions.config.Config
import shd.asciiwebcam.functions.console.Console.{clearConsole, printImage}
import shd.asciiwebcam.functions.image.Image.{compressTo, toAsciiSequence}
import shd.asciiwebcam.functions.rich.Rich.RichInt
import shd.asciiwebcam.functions.webcam.Webcam.{webcamGrabber, webcamImage}

object Launcher extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for (
      config <- Config.read;
      terminal <- TerminalBuilder.terminal().pure[IO];
      image <- webcamImage(webcamGrabber(config.device));
      resizedImage <- compressTo(terminal.getWidth.ifZero(config.defaultWidth), terminal.getHeight.ifZero(config.defaultHeight), image);
      asciiList <- toAsciiSequence(resizedImage, config);
      _ <- printImage(asciiList)
    ) yield ExitCode.Success

}
