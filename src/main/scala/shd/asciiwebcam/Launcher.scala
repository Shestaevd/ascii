package shd.asciiwebcam

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits.catsSyntaxApplicativeId
import org.jline.terminal.TerminalBuilder
import shd.asciiwebcam.utils.config.Config
import shd.asciiwebcam.utils.image.Image.{readImage, toAsciiSequence}
import shd.asciiwebcam.utils.image.ImageCompress.compressTo

object Launcher extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for (
      terminal <- TerminalBuilder.terminal().pure[IO];
      config <- Config.read;
      image <- readImage(config.imagePath);
      resizedImage <- compressTo(if (terminal.getWidth != 0) terminal.getWidth else config.defaultWidth, image);
      asciiList <- toAsciiSequence(resizedImage, config);
      _ <- Resource.make(terminal.writer().pure[IO])(_.flush().pure[IO]).use(t =>
        IO(asciiList.foreach { h =>
          h.foreach(w => t.write(w))
          t.write("\n")
        })
      )
    ) yield ExitCode.Success
}
