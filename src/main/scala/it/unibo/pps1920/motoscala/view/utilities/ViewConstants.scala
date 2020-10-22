package it.unibo.pps1920.motoscala.view.utilities

import scalafx.scene.paint.Color
import scalafx.stage.Screen

/** Constants for the view */
protected[view] object ViewConstants {
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
      val BumperCar: String = EntitiesFolder + "BumperCar.png"
      val BumperCarMP: String = EntitiesFolder + "BumperCarMP.png"
      val RedPupa: String = EntitiesFolder + "RedPupa.png"
      val BlackPupa: String = EntitiesFolder + "BlackPupa.png"
      val BluePupa: String = EntitiesFolder + "BluePupa.png"
      val Polar: String = EntitiesFolder + "Polar.png"
      val BigBoss: String = EntitiesFolder + "BigBoss.png"
      val Nabicon: String = EntitiesFolder + "Nabicon.png"
      val Beecon: String = EntitiesFolder + "Beecon.png"
      val PowerUpJump: String = EntitiesFolder + "PowerUpJump.png"
      val PowerUpWeight: String = EntitiesFolder + "PowerUpWeight.png"
      val PowerUpSpeed: String = EntitiesFolder + "PowerUpSpeed.png"
    }
  }
}
