package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{ActorRef, ActorSelection, Props, Timers}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandEvent, CommandableEvent}
import it.unibo.pps1920.motoscala.controller.mediation.{EventObserver, Mediator}
import it.unibo.pps1920.motoscala.multiplayer.actors.ClientActor.Constants.ResponseTime
import it.unibo.pps1920.motoscala.multiplayer.actors.ClientActor.SchedulerTickKey
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage._
import it.unibo.pps1920.motoscala.view.events.ViewEvent.{LeaveLobbyEvent, LevelSetupEvent, LobbyDataEvent}
import it.unibo.pps1920.motoscala.view.showSimplePopup
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._
import scala.language.postfixOps

private class ClientActor(
  protected val actorController: ActorController)
  extends ActorWhitLogging with Timers with EventObserver[CommandableEvent] {

  import MagicValues._

  override protected val logger: Logger = LoggerFactory getLogger classOf[ClientActor]
  private val levelMediator: Mediator = actorController.getMediator
  private var serverActor: Option[ActorRef] = None
  private var serverAddress: ActorSelection = _

  def handle(commandEvent: CommandEvent): Unit = serverActor.get ! CommandableActorMessage(commandEvent)
  override def receive: Receive = idleBehaviour

  private def idleBehaviour: Receive = {
    case TryJoin(selection, name) =>
      serverAddress = context.system.actorSelection(selection)
      serverAddress ! JoinRequestActorMessage(name)
      timers.startSingleTimer(SchedulerTickKey, TimeOut, ResponseTime seconds)
    case rdyMsg: ReadyActorMessage => serverActor.get ! rdyMsg
    case reqMsg: JoinRequestActorMessage => serverActor.get ! reqMsg
    case JoinResponseActorMessage(None) =>
      timers.cancelAll()
      changeBehaviourWhitLogging(initMatchBehaviour)
      serverActor = Some(sender())
      actorController.joinResult(true)
    case JoinResponseActorMessage(Some(error)) =>
      timers.cancelAll()
      sendViewMessage(error.title, error.text)
      actorController.joinResult(false)
    case TimeOut =>
      sendViewMessage(ServerNotFoundMsgTitle, ServerNotFoundMsgText)
      actorController.joinResult(false)

  }

  private def initMatchBehaviour: Receive = {
    //Update view lobby data
    case LobbyDataActorMessage(lobbyData) => actorController.sendToViewStrategy(_.notify(LobbyDataEvent(lobbyData)))
    case readyMsg: ReadyActorMessage => serverActor.get ! readyMsg
    case GameStartActorMessage() =>
      changeBehaviourWhitLogging(inGameBehaviour)
      actorController.gameStart()
      levelMediator.subscribe(this)
    case KickActorMessage(_) => actorController.gotKicked()
    case ev: LeaveEvent =>
      serverActor.get ! ev
      actorController.shutdownMultiplayer()
    case CloseLobbyActorMessage() =>
      actorController.sendToViewStrategy(_.notify(LeaveLobbyEvent()))
      sendViewMessage(LobbyClosedMsgTitle, LobbyClosedMsgText)
      actorController.shutdownMultiplayer()
  }

  private def inGameBehaviour: Receive = {
    case GameEndActorMessage => actorController.gameEnd()
    case DisplayableActorMessage(event) => actorController.getMediator.publishEvent(event)
    case LevelSetupMessage(levelSetupData) =>
      actorController.sendToViewStrategy(_.notify(LevelSetupEvent(levelSetupData)))
  }

  private def sendViewMessage(title: String, text: String): Unit = showSimplePopup(text, title)
  /**
   * Notify the observer with the event.
   *
   * @param event the notified event.
   */
  override def notify(event: CommandableEvent): Unit = serverActor.get ! CommandableActorMessage(event)

  private[this] object MagicValues {
    final val LobbyClosedMsgTitle = "Lobby has been closed"
    final val LobbyClosedMsgText = "Choose another server"
    final val ServerNotFoundMsgTitle = "Cannot find server"
    final val ServerNotFoundMsgText = "Timeout reached"
  }
}

object ClientActor {

  def props(controller: ActorController): Props = Props(new ClientActor(controller))
  object Constants {
    final val ResponseTime = 5
  }

  private case object SchedulerTickKey
}
