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


  /** Entities constants */
  object Entities {

    /** Colors constants */
    object Colors {
      val DefaultPlayerColor: Color = Color.Green
    }

    /** Textures constants */
    object Textures {
      val TextureFolder: String = "/textures/"
      val BackgroundTexture: String = TextureFolder + "background.png"
      val ParticleTexture: String = TextureFolder + "particle.png"
    }
  }
}
