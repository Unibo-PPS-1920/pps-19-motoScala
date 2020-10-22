package it.unibo.pps1920.motoscala.view.drawable

import javafx.scene.image.Image

/** This trait represent a Drawable which contains an image */
trait ImageDrawable extends Drawable {
  def image: Image
}
