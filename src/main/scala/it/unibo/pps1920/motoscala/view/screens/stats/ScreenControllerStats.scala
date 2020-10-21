package it.unibo.pps1920.motoscala.view.screens.stats

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.events.ViewEvent.ScoreDataEvent

/** Screen controller for stats FXML.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
final class ScreenControllerStats(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends AbstractScreenControllerStats(viewFacade, controller) {

  override def whenDisplayed(): Unit = controller.loadStats()

  override def notify(ev: ViewEvent): Unit = ev match {
    case ScoreDataEvent(score) => populateScoreBoard(score)
    case _ =>
  }
}
