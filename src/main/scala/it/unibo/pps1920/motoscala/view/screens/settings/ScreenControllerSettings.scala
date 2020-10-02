package it.unibo.pps1920.motoscala.view.screens.settings

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent


final class ScreenControllerSettings(protected override val viewFacade: ViewFacade,
                                     protected override val controller: ObservableUI) extends AbstractScreenControllerSettings(viewFacade, controller) {
  logger info "Settings Screen"

  override def whenDisplayed(): Unit = {}

  override def notify(ev: ViewEvent): Unit = ev match {
    case event: ViewEvent.SettingsEvent => logger info event.toString
    case _ =>
  }
}
