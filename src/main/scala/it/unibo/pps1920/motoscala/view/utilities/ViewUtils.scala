package it.unibo.pps1920.motoscala.view.utilities

import it.unibo.pps1920.motoscala.view.utilities.ViewConstants.{SCREEN_MIN_HEIGHT, SCREEN_MIN_WIDTH}
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.{Stage, StageStyle, WindowEvent}

private[view] object ViewUtils {
  def createStage(
    scene: Scene,
    minDim: (Int, Int) = (SCREEN_MIN_WIDTH, SCREEN_MIN_HEIGHT),
    icon: Image = new Image(ViewConstants.STAGE_ICON_PATH),
    onCloseRequest: EventHandler[WindowEvent] = _ => System.exit(0)
  ): Stage = {
    val stage = new Stage(StageStyle.DECORATED)
    stage.setScene(scene)
    stage.getIcons.add(icon)
    stage.show()
    stage setMinWidth minDim._1
    stage setMinHeight minDim._2
    stage.centerOnScreen()
    stage setOnCloseRequest onCloseRequest
    stage
  }
}
