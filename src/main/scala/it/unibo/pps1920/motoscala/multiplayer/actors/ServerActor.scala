package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{ActorRef, Props}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Event.DisplayableEvent
import it.unibo.pps1920.motoscala.controller.mediation.{EventObserver, Mediator}
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage._
import it.unibo.pps1920.motoscala.multiplayer.messages.ErrorMsg.ErrorReason
import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LobbyDataEvent
import org.slf4j.{Logger, LoggerFactory}
class ServerActor(
  protected val actorController: ActorController) extends ActorWhitLogging with EventObserver[DisplayableEvent] {

  override protected val logger: Logger = LoggerFactory getLogger classOf[ServerActor]
  private val mediator: Mediator = actorController.mediator
  private var clients: Set[ActorRef] = Set()
  override def receive: Receive = idleBehaviour

  def idleBehaviour: Receive = {
    case mess: LobbyDataActorMessage => tellToClients(mess)

    case JoinRequestActorMessage(name) => handleJoinRequest(name)

    case ReadyActorMessage(status) =>
      actorController.sendToLobbyStrategy(_.setPlayerStatus(sender(), status))
      updateClients()

    case KickActorMessage(ref) => ref ! KickActorMessage(ref);

    case LeaveEvent(ref) =>
      actorController.sendToLobbyStrategy(lobby => lobby.removePlayer(lobby.getPlayerStatus(ref).name))
      val readyPlayersData = actorController.getLobbyData.readyPlayers
      actorController.sendToViewStrategy(_.notify(LobbyDataEvent(LobbyData(readyPlayers = readyPlayersData))))
      tellToClients(LobbyDataActorMessage(LobbyData(readyPlayers = readyPlayersData)))

    case ev: CloseLobbyActorMessage =>
      tellToClients(ev)
      actorController.shutdownMultiplayer()

    case ev: GameStartActorMessage =>
      mediator.subscribe(this)
      changeBehaviourWhitLogging(inGameBehaviour)
      tellToClients(ev)
  }

  def inGameBehaviour: Receive = {
    case CommandableActorMessage(event) => mediator.publishEvent(event)
    case SetupsForClientsMessage(setups) => clients.foreach(_ ! LevelSetupMessage(setups.iterator.next()))
  }

  private def handleJoinRequest(name: String): Unit = {
    /*
        val resp = controller.requestJoin(sender())
    */
    val err = actorController.sendToLobbyStrategy[Option[ErrorReason]](_.tryAddPlayer(sender(), name))

    sender() ! JoinResponseActorMessage(err)
    if (err.isEmpty) {
      clients += sender()
      updateClients()
    }
  }

  private def updateClients(): Unit = {
    val lobbyData = actorController.getLobbyData
    actorController.sendToViewStrategy(_.notify(ViewEvent.LobbyDataEvent(lobbyData)))
    tellToClients(LobbyDataActorMessage(lobbyData))
  }

  private def tellToClients(mess: ActorMessage): Unit = clients.foreach(_ ! mess)
  /**
   * Notify the observer with the event.
   *
   */
  override def notify(event: DisplayableEvent): Unit = clients.foreach(_ ! DisplayableActorMessage(event))
}

object ServerActor {
  def props(controller: ActorController): Props = Props(new ServerActor(controller))
}
