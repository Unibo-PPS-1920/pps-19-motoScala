package it.unibo.pps1920.motoscala.view.drawable

import it.unibo.pps1920.motoscala.view.loaders.ImageLoader
import it.unibo.pps1920.motoscala.view.utilities.ViewConstants.Entities.Textures

/**
 * Drawables game textures
 */
private[view] object Drawables {
  val PlayerDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BumperCar))
  val PlayerMPDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BumperCarMP))
  val BlackPupaDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BlackPupa))
  val BluePupaDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BluePupa))
  val RedPupaDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.RedPupa))
  val PolarDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Polar))
  val NabiconDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Nabicon))
  val BeeconDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Beecon))
  val PowerUpJumpDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.PowerUpJump))
  val PowerUpWeightDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.PowerUpWeight))
  val PowerUpSpeedDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.PowerUpSpeed))
}
