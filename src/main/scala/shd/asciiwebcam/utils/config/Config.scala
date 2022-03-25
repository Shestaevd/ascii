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

case class Config(imagePath: String, defaultWidth: Int, asciiSeq: List[Char] = "№@#W$9876543210?!abc;:+=-,._ ".toList)
