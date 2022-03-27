package shd.asciiwebcam.functions.webcam

import cats.effect._
import org.bytedeco.javacv._

import java.awt.image.BufferedImage

object Webcam {

  def grabber(device: Int): Resource[IO, OpenCVFrameGrabber] =
    Resource.make(IO {
      val grabber = new OpenCVFrameGrabber(device)
      grabber.start()
      grabber
    }) { grabber =>
      IO {
        grabber.close()
      }
    }

  def webcamImage(grabber: OpenCVFrameGrabber): IO[BufferedImage] =
    IO(new Java2DFrameConverter().convert(grabber.grab()))

}
