package it.unibo.pps1920.motoscala.view.utilities

import javafx.stage

object ViewConstants {
  val SCREEN_HEIGHT: Double = stage.Screen.getScreens.get(0).getBounds.getHeight
  val SCREEN_WIDTH: Double = stage.Screen.getScreens.get(0).getBounds.getWidth
  val SCREEN_MIN_WIDTH: Int = 700
  val SCREEN_MIN_HEIGHT: Int = 800
  val STAGE_ICON_PATH = "/images/Icon.png"
}
