package it.unibo.pps1920.motoscala.view.screens.`end`

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.model.Scores.ScoresData
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.layout.{AnchorPane, BorderPane}

abstract class AbstractScreenControllerEndGame(protected override val viewFacade: ViewFacade,
                                               protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  @FXML protected var root: BorderPane = _
  @FXML protected var listView: ListView[String] = _
  @FXML protected var mainAnchorPane: AnchorPane = _

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    initBackButton()
  }

  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'EndGame.fxml'.")
    assert(mainAnchorPane != null, "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'EndGame.fxml'.")
    assert(listView != null, "fx:id=\"listView\" was not injected: check your FXML file 'EndGame.fxml'.")
  }

  protected def populateScoreBoard(score: ScoresData): Unit = {
    this.listView.getItems.clear()
    score.scoreTable.toList.map(userScore => s"${
      userScore._1
    } \t ${userScore._2}").foreach(field => this.listView.getItems.add(field))

  }


}

