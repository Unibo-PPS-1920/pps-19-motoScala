package it.unibo.pps1920.motoscala.view.screens.`end`

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent


final class ScreenControllerEndGame(protected override val viewFacade: ViewFacade,
                                    protected override val controller: ObservableUI) extends AbstractScreenControllerEndGame(viewFacade, controller) {
  logger info "Lobby Screen"

  override def whenDisplayed(): Unit = {}

  override def notify(ev: ViewEvent): Unit = ev match {
    case event: ViewEvent.LobbyEvent => logger info event.toString
    case _ =>
  }
}
