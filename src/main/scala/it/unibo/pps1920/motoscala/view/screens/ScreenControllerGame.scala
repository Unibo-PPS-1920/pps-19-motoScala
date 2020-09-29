package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EntityData, LevelEndData, LevelSetupData}
import it.unibo.pps1920.motoscala.controller.mediation.{Displayable, Event, Mediator}
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.screens.abstracts.AbstractScreenControllerGame

class ScreenControllerGame(protected override val viewFacade: ViewFacade,
                           protected override val controller: ObservableUI)
  extends AbstractScreenControllerGame(viewFacade, controller) with Displayable {
  private val mediator: Mediator = controller.getMediator

  mediator.subscribe(this)

  override def notifyDrawEntities(entities: Seq[EntityData]): Unit = ???
  override def notifyLevelSetup(data: LevelSetupData): Unit = ???
  override def notifyLevelEnd(data: LevelEndData): Unit = ???

  override def notify(ev: ViewEvent): Unit = ???
  override def sendCommandEvent(event: Event.CommandEvent): Unit = mediator.publishEvent(event)
}
