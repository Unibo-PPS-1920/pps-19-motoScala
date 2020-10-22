package it.unibo.pps1920.motoscala.view.screens.stats

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.model.Scores.ScoresData
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.layout.{AnchorPane, BorderPane}

/** Abstract ScreenController dedicated to show Stats View.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
protected[stats] abstract class AbstractScreenControllerStats(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {

  @FXML protected var root: BorderPane = _
  @FXML protected var mainAnchorPane: AnchorPane = _
  @FXML protected var listView: ListView[String] = _

  @FXML override def initialize(): Unit = {
    assertNodeInjected(root, mainAnchorPane, listView)
    assertNodeInjected()
    initBackButton()
  }

  protected def populateScoreBoard(score: ScoresData): Unit = {
    listView.getItems.clear()
    score.scoreTable.toList.map(userScore => s"${userScore._1} \t ${userScore._2}").foreach(listView.getItems.add(_))
  }
}

