package shd.asciiwebcam.utils.config

import cats.effect.IO
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Config {

  def read: IO[Config] =
    ConfigSource
      .resources("configuration.conf")
      .load[Config]
      .fold(fa => IO.raiseError(new Exception(fa.toList.map(_.description).mkString(" ; "))), IO(_))

}

case class Config(imagePath: String, defaultWidth: Int, defaultHeight: Int, reverse: Boolean = false, asciiSeq: String = "№@964?!a;:+=-,._  ")
