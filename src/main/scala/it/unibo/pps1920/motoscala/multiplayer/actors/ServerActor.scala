package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Event.DisplayableEvent
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.multiplayer.messages.ErrorMsg.ErrorReason
import it.unibo.pps1920.motoscala.multiplayer.messages.Message
import it.unibo.pps1920.motoscala.multiplayer.messages.Message._
import it.unibo.pps1920.motoscala.view.events.ViewEvent
class ServerActor(protected val actorController: ActorController) extends Actor with ActorLogging {

  import org.slf4j.LoggerFactory
  private val controller: ActorController = actorController
  private val levelMediator: Mediator = controller.getMediator
  private val logger = LoggerFactory getLogger classOf[ServerActor]
  private var clients: Set[ActorRef] = Set()
  def handle(displayEvent: DisplayableEvent): Unit = clients
    .foreach(c => c ! DisplayMessage(displayEvent))

  override def receive: Receive = idleBehaviour
  def idleBehaviour: Receive = {
    case PlainMessage(text, num) => println("TESTO: " + text + "NUM:" + num)
    case mess: LobbyDataMessage => tellToClients(mess)
    case JoinRequestMessage(name) => handleJoinRequest(name)
    case ReadyMessage(status) =>
      this.controller.sendToLobbyStrategy(lobby => {
        lobby.setPlayerStatus(this.sender(), status)
      })
      this.updateClients()
    case msg => logger warn s"Received unexpected message IDLE $msg"
  }
  private def handleJoinRequest(name: String): Unit = {
    /*
        val resp = this.controller.requestJoin(this.sender())
    */
    val err = this.controller.sendToLobbyStrategy[Option[ErrorReason]](strategy => {
      strategy.tryAddPlayer(this.sender(), name)

    })

    this.sender() ! JoinResponseMessage(err)

    if (err.isEmpty) {
      this.clients += this.sender()
      this.updateClients()
    }
  }
  private def updateClients(): Unit = {
    val lobbyData = controller.getLobbyData
    this.controller.sendToViewStrategy(view => view.notify(ViewEvent.LobbyDataEvent(lobbyData)))
    tellToClients(LobbyDataMessage(lobbyData))
  }
  private def tellToClients(mess: Message): Unit = {
    this.clients.foreach(c => c ! mess)
  }
  def inGameBehaviour: Receive = {
    case CommandMessage(event) => controller.getMediator.publishEvent(event)
    case msg => logger warn s"Received unexpected message $msg"
      println(akka.serialization.Serialization
                .serializedActorPath(self))
  }

}

object ServerActor {
  def props(controller: ActorController): Props = Props(new ServerActor(controller))
}
