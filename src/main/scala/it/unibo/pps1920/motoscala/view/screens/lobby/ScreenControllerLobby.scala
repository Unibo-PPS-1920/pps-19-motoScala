package it.unibo.pps1920.motoscala.view.screens.lobby

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent


final class ScreenControllerLobby(protected override val viewFacade: ViewFacade,
                                  protected override val controller: ObservableUI) extends AbstractScreenControllerLobby(viewFacade, controller) {
  logger info "Lobby Screen"

  override def whenDisplayed(): Unit = {}

  override def notify(ev: ViewEvent): Unit = ev match {
    case event: ViewEvent.LobbyEvent => logger info event.toString
    case _ =>
  }
}
