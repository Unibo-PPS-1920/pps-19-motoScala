package it.unibo.pps1920.motoscala.view.screens.levels

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LevelDataEvent

/** Screen controller for levels FXML.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
protected[view] final class ScreenControllerLevels(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends AbstractScreenControllerLevels(viewFacade, controller) {

  override def whenDisplayed(): Unit = controller.loadAllLevels()

  override def notify(ev: ViewEvent): Unit = ev match {
    case LevelDataEvent(levels) => populateLevels(levels)
    case _ =>
  }
}
