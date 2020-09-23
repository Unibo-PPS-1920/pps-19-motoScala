package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.UpdatableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane

final class ScreenControllerHome(protected override val viewFacade: ViewFacade,
                                 protected override val controller: UpdatableUI) extends ScreenController(viewFacade, controller) {
  @FXML protected var root: BorderPane = _

  viewFacade.loadFXMLNode(FXMLScreens.HOME, this)
  logger info "Home Screen"

  @FXML override def initialize(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Home.fxml'.")
  }
  override def notify(ev: ViewEvent): Unit = ev match {
    case event: ViewEvent.HomeEvent => logger info "Home message"
    case _ =>
  }
}
