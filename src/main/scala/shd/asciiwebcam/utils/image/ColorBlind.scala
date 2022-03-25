package shd.asciiwebcam.utils.image

import java.awt.Color
import java.awt.image.BufferedImage

object ColorBlind {

  def removeColor(bufferedImage: BufferedImage): BufferedImage = {
    val h = bufferedImage.getHeight
    val w = bufferedImage.getWidth

    for (i <- 0 until h) {
      for (j <- 0 until w) {

        val color = new Color(bufferedImage.getRGB(j, i))

        val avgVal = (color.getRed + color.getGreen + color.getBlue) / 3

        val avg = new Color(avgVal, avgVal, avgVal, 255)

        bufferedImage.setRGB(j, i, avg.getRGB)

      }
    }
    bufferedImage
  }

}
