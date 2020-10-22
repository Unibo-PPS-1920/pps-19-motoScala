package it.unibo.pps1920.motoscala.view.screens.game

import it.unibo.pps1920.motoscala.view.drawable.EntityDrawable
import it.unibo.pps1920.motoscala.view.loaders.ImageLoader
import it.unibo.pps1920.motoscala.view.utilities.ViewConstants.Entities.Textures

/**
 * Drawables game textures
 */
private[game] object Drawables {
  val PlayerDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BumperCar))
  val PlayerMPDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BumperCarMP))
  val BlackPupaDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BlackPupa))
  val BluePupaDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BluePupa))
  val RedPupaDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.RedPupa))
  val PolarDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Polar))
  val Block2Drawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Block2))
  val Block1Drawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Block1))
  val JumpDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Jump))
  val WeightDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Weight))
  val SpeedDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Speed))
}
