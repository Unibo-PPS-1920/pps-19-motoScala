package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.UpdatableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent

final class ScreenControllerHome(protected override val viewFacade: ViewFacade,
                                 protected override val controller: UpdatableUI) extends AbstractScreenControllerHome(viewFacade, controller) {
  viewFacade.loadFXMLNode(FXMLScreens.HOME, this)
  logger info "Home Screen"

  override def notify(ev: ViewEvent): Unit = ev match {
    case event: ViewEvent.HomeEvent => logger info "Home message"
    case _ =>
  }
}
