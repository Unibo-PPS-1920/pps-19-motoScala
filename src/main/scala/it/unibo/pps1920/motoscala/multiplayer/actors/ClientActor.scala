package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.multiplayer.messages.Message._
private class ClientActor(protected val actorController: ActorController) extends Actor with ActorLogging{
  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
  import org.slf4j.LoggerFactory
  private var serverActor : Option[ActorRef] = None
  private val controller : ActorController = actorController
  private val levelMediator : Mediator = controller.getMediator
  private val logger = LoggerFactory getLogger classOf[ClientActor]

  //def connectToServer() = ???
  def handle(commandEvent: CommandEvent): Unit = serverActor.get.tell(CommandMessage(commandEvent), this.self)

  override def receive: Receive = idleBehaviour

   private def inGameBehaviour: Receive = {
    case GameEndMessage => controller.gameEnd()
    case DisplayMessage(event) => controller.getMediator.publishEvent(event)
    case msg => logger warn s"Received unexpected message ${msg}"
  }

  private def initMatchBehaviour: Receive ={
    case LobbyDataMessage(lobbyData) => controller.settingsUpdate(lobbyData)
    case readyMsg:ReadyMessage => serverActor.get.tell(readyMsg, this.self)
    case GameStartMessage => controller.gameStart(); this.context.become(inGameBehaviour)
    case msg => logger warn s"Received unexpected message ${msg}"
  }
  private def idleBehaviour: Receive = {
    case reqMsg:JoinRequestMessage => serverActor.get.tell(reqMsg, this.self)
    case JoinResponseMessage(true) => this.context.become(initMatchBehaviour)
    case JoinResponseMessage(false) => ???
    case msg => logger warn s"Received unexpected message ${msg}"
  }
}

object ClientActor{
  import akka.actor.Props
  def props(controller: ActorController): Props = Props(new ClientActor(controller))
}