package shd.asciiwebcam

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits.catsSyntaxApplicativeId
import com.github.sarxos.webcam.Webcam
import org.jline.terminal.TerminalBuilder
import shd.asciiwebcam.utils.config.Config
import shd.asciiwebcam.utils.image.Image.{readImage, toAsciiSequence, webcamImage}
import shd.asciiwebcam.utils.image.ImageCompress.compressTo
import shd.asciiwebcam.utils.rich.Rich.RichInt

object Launcher extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for (
      terminal <- TerminalBuilder.terminal().pure[IO];
      config <- Config.read;
      image <- readImage(config.imagePath);
      resizedImage <- compressTo(terminal.getWidth.ifEmpty(config.defaultWidth), terminal.getHeight.ifEmpty(config.defaultHeight), image);
      asciiList <- toAsciiSequence(resizedImage, config);
      _ <- {
          IO(asciiList.foreach { h =>
            h.foreach(w => print(w))
            print("\n")
          })
      }
    ) yield ExitCode.Success
}
