package shd.asciiwebcam.utils.image

import cats.effect.IO
import cats.implicits.{catsSyntaxApplicativeId, toTraverseOps}
import com.github.sarxos.webcam.Webcam
import shd.asciiwebcam.utils.config.Config
import shd.asciiwebcam.utils.rich.Rich.RichList

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.util.Try

object Image {

  def getAvgColor(color: Color): Int = (color.getRed + color.getGreen + color.getBlue) / 3

  def readImage(path: String): IO[BufferedImage] = ImageIO.read(new File(path)).pure[IO]

  def webcamImage: IO[BufferedImage] = IO {
    val webcam = Webcam.getDefault()
    webcam.open()
    webcam.getImage
  }

  def brightnessToAscii(asciiList: List[Char], reverse: Boolean)(brightness: Int): IO[Char] = IO {
    val rList = asciiList.<>(reverse)
    rList((brightness / 255d * (asciiList.length - 1)).toInt)
  }



  def toAsciiSequence(resizedImage: BufferedImage, config: Config): IO[List[List[Char]]] = {
    (0 until resizedImage.getHeight map ( h =>
      0 until resizedImage.getWidth map ( w =>
        getAvgColor _ andThen brightnessToAscii(config.asciiSeq.toList, config.reverse) apply new Color(resizedImage.getRGB(w, h)))
      )).map(_.toList.sequence).toList.sequence
  }


}
