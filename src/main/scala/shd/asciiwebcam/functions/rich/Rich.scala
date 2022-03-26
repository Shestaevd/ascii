package shd.asciiwebcam.functions.rich

object Rich {

  implicit class RichList[T](l: List[T]) {
    def <> (p: Boolean): List[T] = if(p) l.reverse else l
  }

  implicit class RichInt(i: Int) {
    def ifZero(default: Int): Int = if (i != 0) i else default
  }




}
