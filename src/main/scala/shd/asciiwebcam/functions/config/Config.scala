package shd.asciiwebcam.functions.config

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

case class Config(defaultWidth: Int, defaultHeight: Int, reverse: Boolean = false, asciiSeq: String, device: Int = 0)
