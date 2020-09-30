package it.unibo.pps1920.motoscala.view.screens.levels

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LevelDataEvent
import javafx.application.Platform


final class ScreenControllerLevels(protected override val viewFacade: ViewFacade,
                                   protected override val controller: ObservableUI) extends AbstractScreenControllerLevels(viewFacade, controller) {
  logger info "Level Screen"

  override def whenDisplayed(): Unit = {
    controller.loadAllLevels()
  }

  override def notify(ev: ViewEvent): Unit = ev match {
    case LevelDataEvent(levels) => Platform.runLater(() => populateLevels(levels))
    case _ =>
  }
}
