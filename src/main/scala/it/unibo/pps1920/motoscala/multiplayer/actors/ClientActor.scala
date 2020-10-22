package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging, ActorSelection, Props, Timers}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandableEvent
import it.unibo.pps1920.motoscala.controller.mediation.{EventObserver, Mediator}
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage._
import it.unibo.pps1920.motoscala.view.events.ViewEvent.{LeaveLobbyEvent, LevelSetupEvent, LobbyDataEvent}
import it.unibo.pps1920.motoscala.view.showSimplePopup

import scala.concurrent.duration._
import scala.language.postfixOps
private class ClientActor(protected val actorController: ActorController) extends Actor with ActorLogging with Timers with EventObserver[CommandableEvent] {
  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
  import org.slf4j.LoggerFactory

  private val levelMediator: Mediator = actorController.getMediator
  private val logger = LoggerFactory getLogger classOf[ClientActor]
  private var serverActor: Option[ActorRef] = None
  private var serverAddress: ActorSelection = _
  //def connectToServer() = ???
  def handle(commandEvent: CommandEvent): Unit = serverActor.get ! CommandableActorMessage(commandEvent)
  override def receive: Receive = idleBehaviour
  private def idleBehaviour: Receive = {
    case TryJoin(selection, name) => {
      serverAddress = this.context.system.actorSelection(selection)

      serverAddress ! JoinRequestActorMessage(name)
      timers.startSingleTimer(SchedulerTickKey, TimeOut, 5 seconds)

      /*
            val x = Await.result((serverAddress ask JoinRequestMessage(name)) (timeout), Duration.Inf)
      */
      /*      x match {
              case JoinResponseMessage(true) => {
                this.context.become(initMatchBehaviour);
                this.serverActor = Some(this.sender())
                this.actorController.joinResult(true)

              }
              case JoinResponseMessage(false) => this.actorController.joinResult(false)
              case _ =>
            }*/


    }
    case rdyMsg: ReadyActorMessage => serverActor.get ! rdyMsg
    case reqMsg: JoinRequestActorMessage => serverActor.get ! reqMsg
    case JoinResponseActorMessage(None) => {
      timers.cancelAll()
      this.context.become(initMatchBehaviour);
      this.serverActor = Some(this.sender())
      this.actorController.joinResult(true)
    }
    case JoinResponseActorMessage(Some(error)) => {
      timers.cancelAll()
      sendViewMessage(error.title, error.text)
      this.actorController.joinResult(false)

    }
    case TimeOut => sendViewMessage("Cannot find server", "Timeout reached"); this.actorController.joinResult(false)


    /*
          this.actorController.joinResult(false)
    */
    case msg => logger warn s"Received unexpected message $msg"
  }
  private def initMatchBehaviour: Receive = {
    //Update view lobby data
    case LobbyDataActorMessage(lobbyData) => actorController
      .sendToViewStrategy(obsUi => obsUi.notify(LobbyDataEvent(lobbyData)))
    case readyMsg: ReadyActorMessage => serverActor.get ! readyMsg
    case GameStartActorMessage() => {
      this.context.become(inGameBehaviour)
      actorController.gameStart()
      this.levelMediator.subscribe(this)
    }
    case KickActorMessage(_) => {
      this.actorController.gotKicked()
    }
    case ev: LeaveEvent => {
      this.serverActor.get ! ev
      this.actorController.shutdownMultiplayer()
    }
    case CloseLobbyActorMessage() => {
      this.actorController.sendToViewStrategy(obsUi => obsUi.notify(LeaveLobbyEvent()))
      sendViewMessage("Lobby has been closed", "Choose another server")
      this.actorController.shutdownMultiplayer()
    }
    case msg => logger warn s"Received unexpected message $msg"
  }
  private def inGameBehaviour: Receive = {
    case GameEndActorMessage => actorController.gameEnd()
    case DisplayableActorMessage(event) => actorController.getMediator.publishEvent(event)
    case LevelSetupMessage(levelSetupData) =>
      actorController.sendToViewStrategy(obsUI => obsUI.notify(LevelSetupEvent(levelSetupData)))
    case msg => logger warn s"Received unexpected message $msg"
  }
  private def sendViewMessage(title: String, text: String): Unit = showSimplePopup(text, title)
  /**
   * Notify the observer with the event.
   *
   * @param event the notified event.
   */
  override def notify(event: CommandableEvent): Unit = serverActor.get ! CommandableActorMessage(event)
  private case object SchedulerTickKey
}

object ClientActor {
  def props(controller: ActorController): Props = Props(new ClientActor(controller))
}
