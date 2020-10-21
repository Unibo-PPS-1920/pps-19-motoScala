package it.unibo.pps1920.motoscala.view.screens.game

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EndData, EntityData, LifeData, SoundEvent}
import it.unibo.pps1920.motoscala.controller.mediation.{Displayable, Event, Mediator}
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LevelSetupEvent
import javafx.application.Platform

class ScreenControllerGame(protected override val viewFacade: ViewFacade,
                           protected override val controller: ObservableUI)
  extends AbstractScreenControllerGame(viewFacade, controller) with Displayable {
  private val mediator: Mediator = controller.mediator
  logger info "Game Screen"
  mediator.subscribe(this)
  //From Mediator
  override def notifyDrawEntities(players: Set[Option[EntityData]], entities: Set[EntityData]): Unit =
    Platform.runLater(() => handleDrawEntities(players, entities))
  override def notifyLevelEnd(data: EndData): Unit = Platform.runLater(() => handleLevelEnd(data))
  override def notifyEntityLife(data: LifeData): Unit = Platform.runLater(() => handleEntityLife(data))
  override def notifyRedirectSound(event: SoundEvent): Unit = controller.redirectSoundEvent(event)
  //From ViewFacade
  override def notify(ev: ViewEvent): Unit = ev match {
    case LevelSetupEvent(data) => Platform.runLater(() => handleLevelSetup(data))
    case _ => logger info "Unexpected message"
  }
  //To Mediator
  override def sendCommandEvent(event: Event.CommandEvent): Unit = mediator.publishEvent(event)
}
