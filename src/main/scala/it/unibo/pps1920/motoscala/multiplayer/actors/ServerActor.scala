package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Event.DisplayableEvent
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage._
import it.unibo.pps1920.motoscala.multiplayer.messages.ErrorMsg.ErrorReason
import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LeaveEvent
class ServerActor(protected val actorController: ActorController) extends Actor with ActorLogging {

  import org.slf4j.LoggerFactory
  private val levelMediator: Mediator = actorController.getMediator
  private val logger = LoggerFactory getLogger classOf[ServerActor]
  private var clients: Set[ActorRef] = Set()
  def handle(displayEvent: DisplayableEvent): Unit = clients
    .foreach(c => c ! DisplayActorMessage(displayEvent))

  override def receive: Receive = idleBehaviour
  def idleBehaviour: Receive = {
    case PlainActorMessage(text, num) => println("TESTO: " + text + "NUM:" + num)
    case mess: LobbyDataActorMessage => tellToClients(mess)
    case JoinRequestActorMessage(name) => handleJoinRequest(name)
    case ReadyActorMessage(status) =>
      this.actorController.sendToLobbyStrategy(lobby => {
        lobby.setPlayerStatus(this.sender(), status)
      })
      this.updateClients()
    case KickActorMessage(ref) => ref ! KickActorMessage(ref);
    case LeaveEvent(ref) => {

      this.actorController.sendToLobbyStrategy(lobby => {
        lobby.removePlayer(lobby.readyPlayers(ref).name)
      })
      this.tellToClients(LobbyDataActorMessage(LobbyData(readyPlayers = this.actorController
        .sendToLobbyStrategy(lobby => {
          lobby.readyPlayers
        }))))
    }
    case ev: CloseLobby => {
      this.tellToClients(ev)
    }
    case msg => logger warn s"Received unexpected message IDLE $msg"
  }
  def inGameBehaviour: Receive = {
    case CommandActorMessage(event) => actorController.getMediator.publishEvent(event)
    case msg => logger warn s"Received unexpected message $msg"
      println(akka.serialization.Serialization
                .serializedActorPath(self))
  }
  private def handleJoinRequest(name: String): Unit = {
    /*
        val resp = this.controller.requestJoin(this.sender())
    */
    val err = this.actorController.sendToLobbyStrategy[Option[ErrorReason]](strategy => {
      strategy.tryAddPlayer(this.sender(), name)

    })

    this.sender() ! JoinResponseActorMessage(err)
    if (err.isEmpty) {
      this.clients += this.sender()
      this.updateClients()
    }
  }
  private def updateClients(): Unit = {
    val lobbyData = actorController.getLobbyData
    this.actorController.sendToViewStrategy(view => view.notify(ViewEvent.LobbyDataEvent(lobbyData)))
    tellToClients(LobbyDataActorMessage(lobbyData))
  }
  private def tellToClients(mess: ActorMessage): Unit = {
    this.clients.foreach(c => c ! mess)
  }

}

object ServerActor {
  def props(controller: ActorController): Props = Props(new ServerActor(controller))
}
