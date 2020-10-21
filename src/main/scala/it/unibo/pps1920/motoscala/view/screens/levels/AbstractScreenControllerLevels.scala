package it.unibo.pps1920.motoscala.view.screens.levels

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import it.unibo.pps1920.motoscala.view.utilities.ViewUtils
import javafx.fxml.FXML
import javafx.scene.layout.{AnchorPane, BorderPane, GridPane}

/** ScreenController dedicated to Levels Screen.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
protected[levels] abstract class AbstractScreenControllerLevels(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {

  @FXML protected final var root: BorderPane = _
  @FXML protected final var mainAnchorPane: AnchorPane = _
  @FXML protected final var grid: GridPane = _

  import MagicValues._
  private object MagicValues {
    val LevelText = "Level"
  }

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    initBackButton()
  }

  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Levels.fxml'.")
    assert(mainAnchorPane != null, "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'Levels.fxml'.")
    assert(grid != null, "fx:id=\"grid\" was not injected: check your FXML file 'Levels.fxml'.")
  }

  private def selectLevel(level: Int): Unit = {
    controller.setupGame(level)
    viewFacade.changeScreen(ChangeScreenEvent.GoNext)
  }

  /** Create the level buttons.
   *
   * @param levels the level data
   */
  protected def populateLevels(levels: Seq[LevelData]): Unit = {
    grid.getChildren.clear()
    levels.map(_.index).sorted.foreach(i => {
      val button = ViewUtils.buttonFactory(bText = s"$LevelText $i",
                                           _ => selectLevel(i),
                                           _ => controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonHover)))
      grid.addRow(i, button)
    })
    grid.addRow(levels.size + 1, buttonBack)
  }
}
