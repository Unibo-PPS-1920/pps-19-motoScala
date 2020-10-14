package it.unibo.pps1920.motoscala.view.screens.modeSelection

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent


final class ScreenControllerModeSelection(protected override val viewFacade: ViewFacade,
                                          protected override val controller: ObservableUI) extends AbstractScreenControllerModeSelection(viewFacade, controller) {
  logger info "Lobby Screen"

  override def whenDisplayed(): Unit = {}


  override def notify(ev: ViewEvent): Unit = ev match {
    case ViewEvent.JoinResult(res) => displayResult(res)
    case _ =>
  }
}
