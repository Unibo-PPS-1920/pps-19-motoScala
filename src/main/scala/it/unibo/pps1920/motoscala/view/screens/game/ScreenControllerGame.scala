package it.unibo.pps1920.motoscala.view.screens.game
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LevelSetupEvent
import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EntityData, LevelEndData, SoundEvent}
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


  override def notifyDrawEntities(player: Set[Option[EntityData]], entities: Set[EntityData]): Unit = Platform.runLater(() => drawEntities(player, entities))

  override def notifyLevelEnd(data: LevelEndData): Unit = Platform.runLater(() => handleTearDown(data))
  override def notifyRedirectSound(event: SoundEvent): Unit = controller.redirectSoundEvent(event)

  override def notify(ev: ViewEvent): Unit = ev match {
    case LevelSetupEvent(data) => Platform.runLater(() => handleSetup(data))
    case _ => logger info "Unexpected message"
  }
  override def sendCommandEvent(event: Event.CommandEvent): Unit = mediator.publishEvent(event)
}
