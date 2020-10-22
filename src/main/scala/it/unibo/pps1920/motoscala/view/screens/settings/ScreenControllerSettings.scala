package it.unibo.pps1920.motoscala.view.screens.settings

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.events.ViewEvent.SettingsDataEvent

/** Screen controller for setting FXML.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
final class ScreenControllerSettings(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends AbstractScreenControllerSettings(viewFacade, controller) {

  override def whenDisplayed(): Unit = controller.loadSetting()

  override def notify(ev: ViewEvent): Unit = ev match {
    case SettingsDataEvent(settings) => displaySettings(settings)
    case _ =>
  }
}
