package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane

abstract class AbstractScreenControllerHome(protected override val viewFacade: ViewFacade,
                                            protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  @FXML protected var root: BorderPane = _

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
  }
  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Home.fxml'.")
  }


}
