package it.unibo.pps1920.motoscala.view.screens.home

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent

/** Screen controller for home FXML.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
final class ScreenControllerHome(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends AbstractScreenControllerHome(viewFacade, controller) {

  override def notify(ev: ViewEvent): Unit = ev match {
    case _ =>
  }


}
