package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Event.DisplayableEvent
import it.unibo.pps1920.motoscala.controller.mediation.{EventObserver, Mediator}
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage._
import it.unibo.pps1920.motoscala.multiplayer.messages.ErrorMsg.ErrorReason
import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LobbyDataEvent
class ServerActor(protected val actorController: ActorController) extends Actor with ActorLogging with EventObserver[DisplayableEvent] {

  import org.slf4j.LoggerFactory
  private val levelMediator: Mediator = actorController.getMediator
  private val logger = LoggerFactory getLogger classOf[ServerActor]
  private var clients: Set[ActorRef] = Set()

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
      val readyPlayersData = this.actorController.getLobbyData.readyPlayers

      this.actorController.sendToViewStrategy(obsUi => {
        obsUi.notify(LobbyDataEvent(LobbyData(readyPlayers = readyPlayersData
                                              )))
      })
      this.tellToClients(LobbyDataActorMessage(LobbyData(readyPlayers = readyPlayersData)))
    }
    case ev: CloseLobbyActorMessage => {
      this.tellToClients(ev)
      this.actorController.shutdownMultiplayer()
    }
    case  ev: GameStartActorMessage => {
      this.levelMediator.subscribe(this)
      this.context.become(inGameBehaviour)
      this.tellToClients(ev)
    }
    case msg => logger warn s"Received unexpected message IDLE $msg"
  }
  def inGameBehaviour: Receive = {
    case CommandableActorMessage(event) => actorController.getMediator.publishEvent(event)
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
  /**
   * Notify the observer with the event.
   *
   * @param event the notified event.
   */
  override def notify(event: DisplayableEvent): Unit = clients
    .foreach(c => c ! DisplayableActorMessage(event))
}

object ServerActor {
  def props(controller: ActorController): Props = Props(new ServerActor(controller))
}
