package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging, ActorSelection, Timers}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.multiplayer.messages.Message._
import it.unibo.pps1920.motoscala.view.JavafxEnums
import it.unibo.pps1920.motoscala.view.events.ViewEvent.{LobbyDataEvent, ShowDialog}

import scala.concurrent.duration._
import scala.language.postfixOps
private class ClientActor(protected val actorController: ActorController) extends Actor with ActorLogging with Timers {
  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
  import org.slf4j.LoggerFactory
  private val controller: ActorController = actorController
  private val levelMediator: Mediator = controller.getMediator
  private val logger = LoggerFactory getLogger classOf[ClientActor]
  private var serverActor: Option[ActorRef] = None
  private var serverAddress: ActorSelection = _
  //def connectToServer() = ???
  def handle(commandEvent: CommandEvent): Unit = serverActor.get ! CommandMessage(commandEvent)
  override def receive: Receive = idleBehaviour
  private def idleBehaviour: Receive = {
    case TryJoin(selection, name) => {
      serverAddress = this.context.system.actorSelection(selection)

      serverAddress ! JoinRequestMessage(name)
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
    case rdyMsg: ReadyMessage => serverActor.get ! rdyMsg
    case reqMsg: JoinRequestMessage => serverActor.get ! reqMsg
    case JoinResponseMessage(None) => {
      timers.cancelAll()
      this.context.become(initMatchBehaviour);
      this.serverActor = Some(this.sender())
      this.actorController.joinResult(true)
    }
    case JoinResponseMessage(Some(error)) => {
      timers.cancelAll()
      sendErrorToView(error.title, error.text)
      this.actorController.joinResult(false)

    }
    case TimeOut => sendErrorToView("Cannot find server", "Timeout reached"); this.actorController.joinResult(false)
    case msg => logger warn s"Received unexpected message ${msg}"
  }
  private def initMatchBehaviour: Receive = {
    //Update view lobby data
    case LobbyDataMessage(lobbyData) => controller
      .sendToViewStrategy(obsUi => obsUi.notify(LobbyDataEvent(lobbyData)))
    case readyMsg: ReadyMessage => serverActor.get ! readyMsg
    case GameStartMessage => controller.gameStart(); this.context.become(inGameBehaviour)
    case msg => logger warn s"Received unexpected message ${msg}"
  }
  private def inGameBehaviour: Receive = {
    case GameEndMessage => controller.gameEnd()
    case DisplayMessage(event) => controller.getMediator.publishEvent(event)
    case msg => logger warn s"Received unexpected message ${msg}"
  }
  private def sendErrorToView(title: String, text: String): Unit = {
    this.controller.sendToViewStrategy(view => view
      .notify(ShowDialog(text, title, JavafxEnums.SHORT_DURATION, JavafxEnums
        .ERROR_NOTIFICATION)))
  }
  private case object SchedulerTickKey
}

object ClientActor {
  import akka.actor.Props
  def props(controller: ActorController): Props = Props(new ClientActor(controller))
}
