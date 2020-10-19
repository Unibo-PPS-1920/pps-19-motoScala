package it.unibo.pps1920.motoscala.view.screens.game

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EntityData, LevelEndData, SoundEvent}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.{EndData, LevelSetupData}
import it.unibo.pps1920.motoscala.controller.mediation.{Displayable, Event, Mediator}
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.{JavafxEnums, ViewFacade, showDialog}
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
  override def notifyLevelEnd(data: LevelEndData): Unit = data match {
    case EndData(true, BumperCarEntity(_), _) => Platform
      .runLater(() => showDialog(this.canvasStack, "hai vinto", controller.updateScore(0).toString, JavafxEnums
        .BIG_DIALOG, null))
    case EndData(false, BumperCarEntity(_), _) => Platform
      .runLater(() => showDialog(this.canvasStack, "hai perso", controller.updateScore(0).toString, JavafxEnums
        .BIG_DIALOG, null))
    case EndData(_, _, score) => Platform.runLater(() => updateScore(score))
  }


  override def notifyRedirectSound(event: SoundEvent): Unit = controller.redirectSoundEvent(event)

  override def notify(ev: ViewEvent): Unit = ???
  override def sendCommandEvent(event: Event.CommandEvent): Unit = mediator.publishEvent(event)
}
