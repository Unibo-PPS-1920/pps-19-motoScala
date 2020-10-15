package it.unibo.pps1920.motoscala.view.screens.game

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EntityData, LevelEndData}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.LevelSetupData
import it.unibo.pps1920.motoscala.controller.mediation.{Displayable, Event, Mediator}
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import javafx.application.Platform

class ScreenControllerGame(protected override val viewFacade: ViewFacade,
                           protected override val controller: ObservableUI)
  extends AbstractScreenControllerGame(viewFacade, controller) with Displayable {
  private val mediator: Mediator = controller.mediator
  logger info "Game Screen"
  mediator.subscribe(this)

  override def notifyLevelSetup(data: LevelSetupData): Unit = Platform.runLater(() => handleSetup(data))
  override def notifyDrawEntities(player: Option[EntityData], entities: Set[EntityData]): Unit = Platform
    .runLater(() => drawEntities(player, entities))
  override def notifyLevelEnd(data: LevelEndData): Unit = {}

  override def notify(ev: ViewEvent): Unit = ???
  override def sendCommandEvent(event: Event.CommandEvent): Unit = mediator.publishEvent(event)
}
