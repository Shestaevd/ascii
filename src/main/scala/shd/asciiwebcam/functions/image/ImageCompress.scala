package shd.asciiwebcam.functions.image

import java.awt.Color
import java.awt.image.BufferedImage
import scala.annotation.tailrec

object ImageCompress {
  private case class PixelBlock(
      xStart: Int,
      yStart: Int,
      xEnd: Int,
      yEnd: Int,
      image: BufferedImage,
      positionX: Int,
      positionY: Int
  )

  private case class Aggregator(r: Int, g: Int, b: Int, count: Int)

  private def compress(pixelBlock: PixelBlock): Color = {
    @tailrec
    def aggRow(xStart: Int, xEnd: Int, y: Int, image: BufferedImage, agg: Aggregator = Aggregator(0, 0, 0, 0)): Aggregator =
      if (xStart <= xEnd) {
        val color = image.getRGB(xStart, y)
        val r = (color >> 16) & 0x000000ff
        val g = (color >> 8) & 0x000000ff
        val b = color & 0x000000ff
        aggRow(xStart + 1, xEnd, y, image, Aggregator(agg.r + r, agg.g + g, agg.b + b, agg.count + 1))
      } else {
        agg
      }

    val aggregated = (pixelBlock.yStart until pixelBlock.yEnd).foldLeft(
      Aggregator(0, 0, 0, 0)
    )((acc, y) => {
      val agg = aggRow(pixelBlock.xStart, pixelBlock.xEnd, y, pixelBlock.image)
      Aggregator(
        acc.r + agg.r,
        acc.g + agg.g,
        acc.b + agg.b,
        agg.count + acc.count
      )
    })

    new Color(
      aggregated.r / aggregated.count,
      aggregated.g / aggregated.count,
      aggregated.b / aggregated.count
    )
  }

  def compress(compression: Int, bufferedImage: BufferedImage): BufferedImage = {
    val height = bufferedImage.getHeight
    val width = bufferedImage.getWidth

    val pixelBlocks =
      for (
        h <- 0 to height by compression if h + compression < height;
        w <- 0 to width by compression if w + compression < width
      )
        yield PixelBlock(w, h, w + compression, h + compression, bufferedImage, w / compression, h / compression)

    val newImage = new BufferedImage(width / compression, height / compression, BufferedImage.TYPE_INT_RGB)

    pixelBlocks.foreach(pixelBlock => {
      val compressedColor = compress(pixelBlock)
      newImage.setRGB(pixelBlock.positionX, pixelBlock.positionY, compressedColor.getRGB)
    })
    newImage
  }

}
