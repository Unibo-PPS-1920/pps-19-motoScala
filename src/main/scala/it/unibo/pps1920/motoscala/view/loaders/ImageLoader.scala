package it.unibo.pps1920.motoscala.view.loaders

import scalafx.scene.image.Image

/** Loader which caches the already requested images */
protected[view] object ImageLoader extends Loader[String, Image] {

  /** This method retrieves an Image by its path.
   *
   * @param path the Image path
   * @return the Image
   */
  def getImage(path: String): Image = get(path, path => new Image(path))
}
