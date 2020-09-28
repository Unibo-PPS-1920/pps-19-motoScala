package it.unibo.pps1920.motoscala.view.utilities

private[view] object ViewUtils {

  final object GlobalViewConstants {
    import javafx.stage
    val SCREEN_HEIGHT: Double = stage.Screen.getScreens.get(0).getBounds.getHeight
    val SCREEN_WIDTH: Double = stage.Screen.getScreens.get(0).getBounds.getWidth
    val SCREEN_MIN_WIDTH: Double = 850
    val SCREEN_MIN_HEIGHT: Double = 650

  }

}
