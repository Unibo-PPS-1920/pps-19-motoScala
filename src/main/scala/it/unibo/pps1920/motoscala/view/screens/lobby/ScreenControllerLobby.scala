package it.unibo.pps1920.motoscala.view.screens.lobby

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent

/** Screen controller for lobby FXML.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
final class ScreenControllerLobby(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends AbstractScreenControllerLobby(viewFacade, controller) {

  override def whenDisplayed(): Unit = {}

  override def notify(ev: ViewEvent): Unit = ev match {
    case ViewEvent.SetupLobbyEvent(ip, port, name, levels, difficulties) => this
      .setIpAndPort(ip, port, name, levels, difficulties)
    case ViewEvent.LobbyDataEvent(lobbyData) => updateLobby(lobbyData)
    case ViewEvent.LeaveLobbyEvent() => leaveLobby()
    case ViewEvent.LoadLevelEvent() => startMulti()
    case _ =>
  }
}
