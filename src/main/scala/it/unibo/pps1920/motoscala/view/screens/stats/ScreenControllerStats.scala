package it.unibo.pps1920.motoscala.view.screens.stats

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent


final class ScreenControllerStats(protected override val viewFacade: ViewFacade,
                                  protected override val controller: ObservableUI) extends AbstractScreenControllerStats(viewFacade, controller) {
  logger info "Stats Screen"

  override def whenDisplayed(): Unit = {}

  override def notify(ev: ViewEvent): Unit = ev match {
    case event: ViewEvent.StatsEvent => logger info event.toString
    case _ =>
  }
}
