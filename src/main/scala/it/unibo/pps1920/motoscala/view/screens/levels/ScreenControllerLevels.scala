package it.unibo.pps1920.motoscala.view.screens.levels

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LevelDataEvent


final class ScreenControllerLevels(protected override val viewFacade: ViewFacade,
                                   protected override val controller: ObservableUI) extends AbstractScreenControllerLevels(viewFacade, controller) {
  logger info "Level Screen"

  override def whenDisplayed(): Unit = {
    //Ask data from controller
    populateLevels(List(LevelData(0, (0, 0), List()), LevelData(1, (0, 0), List()), LevelData(2, (0, 0), List())))
  }

  override def notify(ev: ViewEvent): Unit = ev match {
    case LevelDataEvent(levels) => populateLevels(levels)
    case _ =>
  }
}
