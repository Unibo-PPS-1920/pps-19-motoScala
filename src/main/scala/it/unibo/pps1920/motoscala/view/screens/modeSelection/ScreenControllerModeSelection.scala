package it.unibo.pps1920.motoscala.view.screens.modeSelection

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent

/** Screen controller for modeSelection FXML.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
final class ScreenControllerModeSelection(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends AbstractScreenControllerModeSelection(viewFacade, controller) {

  override def whenDisplayed(): Unit = {}

  override def notify(ev: ViewEvent): Unit = ev match {
    case ViewEvent.JoinResultEvent(res) => displayResult(res)
    case _ =>
  }
}
