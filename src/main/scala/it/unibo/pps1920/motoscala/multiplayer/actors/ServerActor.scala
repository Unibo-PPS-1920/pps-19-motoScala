package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Event.DisplayableEvent
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.multiplayer.messages.Message._
class ServerActor(protected val actorController: ActorController) extends Actor with ActorLogging {

  import org.slf4j.LoggerFactory
  private val controller: ActorController = actorController
  private val levelMediator: Mediator = controller.getMediator
  private val logger = LoggerFactory getLogger classOf[ServerActor]
  private var clients: Set[ActorRef] = Set()
  def handle(displayEvent: DisplayableEvent): Unit = clients
    .foreach(c => c.tell(DisplayMessage(displayEvent), this.self))

  override def receive: Receive = idleBehaviour
  def idleBehaviour: Receive = {
    case PlainMessage(text, num) => println("TESTO: " + text + "NUM:" + num)
    case JoinRequestMessage() => handleJoinRequest()
    case ReadyMessage => controller.setReadyClient(this.sender())
    case msg => logger warn s"Received unexpected message ${msg}"
  }
  private def handleJoinRequest(): Unit = {
    /*
        val resp = this.controller.requestJoin(this.sender())
    */
    val resp = this.controller.sendToLobbyStrategy[Boolean](s => {
      s.tryAddPlayer(this.sender())
    })
    if (resp) {
      this.sender().tell(JoinResponseMessage(resp), this.self)
      this.sender().tell(LobbyDataMessage(controller.getLobbyData), this.self)
    }
  }
  def inGameBehaviour: Receive = {
    case CommandMessage(event) => controller.getMediator.publishEvent(event)
    case msg => logger warn s"Received unexpected message ${msg}";
      println(akka.serialization.Serialization
                .serializedActorPath(self))
  }
}

object ServerActor {
  def props(controller: ActorController): Props = Props(new ServerActor(controller))
}
