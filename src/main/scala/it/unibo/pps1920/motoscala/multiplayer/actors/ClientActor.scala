package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.multiplayer.messages.Message._
private class ClientActor(protected val actorController: ActorController) extends Actor with ActorLogging {
  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
  import org.slf4j.LoggerFactory
  private val controller: ActorController = actorController
  private val levelMediator: Mediator = controller.getMediator
  private val logger = LoggerFactory getLogger classOf[ClientActor]
  private var serverActor: Option[ActorRef] = None
  //def connectToServer() = ???
  def handle(commandEvent: CommandEvent): Unit = serverActor.get ! (CommandMessage(commandEvent), this.self)

  override def receive: Receive = idleBehaviour
  private def idleBehaviour: Receive = {
    case reqMsg: JoinRequestMessage => serverActor.get ! (reqMsg, this.self)
    case JoinResponseMessage(true) => {
      this.context.become(initMatchBehaviour);
      this.serverActor = Some(this.sender())
      this.actorController.joinResult(true)
    }
    case JoinResponseMessage(false) => this.actorController.joinResult(false)
    case msg => logger warn s"Received unexpected message ${msg}"
  }
  private def initMatchBehaviour: Receive = {
    case LobbyDataMessage(lobbyData) => controller.settingsUpdate(lobbyData)
    case readyMsg: ReadyMessage => serverActor.get ! (readyMsg, this.self)
    case GameStartMessage => controller.gameStart(); this.context.become(inGameBehaviour)
    case msg => logger warn s"Received unexpected message ${msg}"
  }
  private def inGameBehaviour: Receive = {
    case GameEndMessage => controller.gameEnd()
    case DisplayMessage(event) => controller.getMediator.publishEvent(event)
    case msg => logger warn s"Received unexpected message ${msg}"
  }


}

object ClientActor {
  import akka.actor.Props
  def props(controller: ActorController): Props = Props(new ClientActor(controller))
}
