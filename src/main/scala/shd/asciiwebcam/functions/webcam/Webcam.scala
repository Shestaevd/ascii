package shd.asciiwebcam.functions.webcam

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import org.bytedeco.javacv.{Java2DFrameConverter, OpenCVFrameGrabber}

import java.awt.image.BufferedImage

object Webcam {

  /*
    Resource.make(IO {
      val grabber = new OpenCVFrameGrabber(device)
      grabber.start()
      grabber
    })(_.close.pure[IO]) == NullPointerException ???
   */

  def webcamGrabber(device: Int): IO[OpenCVFrameGrabber] =
    IO {
      val grabber = new OpenCVFrameGrabber(device)
      grabber.start()
      grabber
    }

  def webcamImage(resource: IO[OpenCVFrameGrabber]): IO[BufferedImage] =
    resource flatMap (_.grab().pure[IO]) flatMap (new Java2DFrameConverter().convert(_).pure[IO])

}