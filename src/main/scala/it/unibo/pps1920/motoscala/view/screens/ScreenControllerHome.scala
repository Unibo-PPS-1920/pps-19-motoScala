package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.view.ViewFacade
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane

final class ScreenControllerHome(private val viewFacade: ViewFacade) {
  @FXML protected var root: BorderPane = _

  viewFacade.loadFXMLNode(FXMLScreens.HOME, this)

  @FXML def initialize(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Home.fxml'.")
  }
}
