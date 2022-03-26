package shd.asciiwebcam.functions.image

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import org.imgscalr.Scalr
import org.imgscalr.Scalr.Mode
import shd.asciiwebcam.functions.config.Config
import shd.asciiwebcam.functions.rich.Rich.RichList

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Image {

  def getAvgColor(color: Color): Int = (color.getRed + color.getGreen + color.getBlue) / 3

  def readImage(path: String): IO[BufferedImage] = ImageIO.read(new File(path)).pure[IO]

  def brightnessToAscii(asciiList: List[Char], isReversed: Boolean)(brightness: Int): Char =
    asciiList <> isReversed apply (brightness / 255d * (asciiList.length - 1)).toInt

  def compressTo(width: Int, height: Int, bufferedImage: BufferedImage): IO[BufferedImage] =
    IO(Scalr.resize(bufferedImage, Mode.FIT_EXACT, width, height))

  def toAsciiSequence(resizedImage: BufferedImage, config: Config): IO[List[List[Char]]] = IO (
    (0 until resizedImage.getHeight map ( h =>
      0 until resizedImage.getWidth map ( w =>
        getAvgColor _ andThen brightnessToAscii(config.asciiSeq.toList, config.reverse) apply new Color(resizedImage.getRGB(w, h)))
      )).map(_.toList).toList
  )

}
