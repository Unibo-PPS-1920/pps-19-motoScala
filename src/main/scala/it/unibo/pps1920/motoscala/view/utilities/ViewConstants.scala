package it.unibo.pps1920.motoscala.view.utilities

import scalafx.scene.paint.Color
import scalafx.stage.Screen

object ViewConstants {
  object Window {
    val Window_title: String = "MotoScala"
    val ScreenWidth: Double = Screen.primary.visualBounds.width
    val HalfScreenWidth: Double = ScreenWidth / 2
    val ScreenHeight: Double = Screen.primary.visualBounds.height
    val HalfScreenHeight: Double = ScreenHeight / 2
    val StageIconPath = "/images/Icon.png"
    val ScreenMinWidth: Int = 700
    val ScreenMinHeight: Int = 800
  }

  object Canvas {
    val CanvasWidth: Int = 800
    val CanvasHeight: Int = 800
  }

  object Entities {
    object Colors {
      val DefaultPlayerColor: Color = Color.Green
    }

    object Textures {
      val TextureFolder: String = "/textures/"
      val EntitiesFolder: String = TextureFolder + "entities/"
      val BackgroundTexture: String = TextureFolder + "background.png"
      val ParticleTexture: String = TextureFolder + "particle.png"
      val BumperCar: String = EntitiesFolder + "entity_22.png"
      val BumperCarMP: String = EntitiesFolder + "entity_15.png"
      val RedPupa: String = EntitiesFolder + "entity_18.png"
      val BlackPupa: String = EntitiesFolder + "entity_19.png"
      val BluePupa: String = EntitiesFolder + "entity_23.png"
      val Polar: String = EntitiesFolder + "entity_20.png"
      val Block2: String = EntitiesFolder + "block2.png"
      val Block1: String = EntitiesFolder + "block1.png"
      val Jump: String = EntitiesFolder + "gem1.png"
      val Weight: String = EntitiesFolder + "gem4.png"
      val Speed: String = EntitiesFolder + "gem2.png"
    }
  }
}
