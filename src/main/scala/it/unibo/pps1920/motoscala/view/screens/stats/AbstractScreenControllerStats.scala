package it.unibo.pps1920.motoscala.view.screens.stats

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.layout.{AnchorPane, BorderPane}

abstract class AbstractScreenControllerStats(protected override val viewFacade: ViewFacade,
                                             protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  @FXML protected var root: BorderPane = _
  @FXML protected var mainAnchorPane: AnchorPane = _
  @FXML protected var listView: ListView[String] = _ //change to complex data

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    initBackButton()
  }

  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Stats.fxml'.")
    assert(mainAnchorPane != null, "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'Stats.fxml'.")
    assert(listView != null, "fx:id=\"listView\" was not injected: check your FXML file 'Stats.fxml'.")
  }
}
