package it.unibo.pps1920.motoscala.multiplayer.actors
import akka.actor.{Actor, ActorLogging}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.multiplayer.messages.Message._
import akka.actor.ActorRef
import it.unibo.pps1920.motoscala.controller.mediation.Event.DisplayableEvent
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import akka.actor.Props
class ServerActor (protected val actorController: ActorController) extends Actor with ActorLogging{

  import org.slf4j.LoggerFactory
  private val controller : ActorController = actorController
  private val levelMediator : Mediator = controller.getMediator
  private var clients : Set[ActorRef] = Set()
  private val logger = LoggerFactory getLogger classOf[ServerActor]
  def handle(displayEvent : DisplayableEvent): Unit = clients.foreach(c=> c.tell(DisplayMessage(displayEvent), this.self))

  override def receive: Receive = idleBehaviour

  def inGameBehaviour : Receive= {
    case CommandMessage(event) => controller.getMediator.publishEvent(event)
    case msg => logger warn s"Received unexpected message ${msg}"; println(akka.serialization.Serialization.serializedActorPath(self))
  }
  def idleBehaviour : Receive = {
    case PlainMessage(text, num) => println("TESTO: "+text + "NUM:" + num)
    case JoinRequestMessage => handleJoinRequest()
    case ReadyMessage => controller.setReadyClient(this.sender())
    case msg => logger warn s"Received unexpected message ${msg}"
  }
  private def handleJoinRequest(): Unit ={
    val resp = controller.requestJoin(this.sender())
    if(resp) {
      this.sender().tell(JoinResponseMessage(resp), this.self)
      this.sender().tell(LobbyDataMessage(controller.getLobbyData), this.self)
    }
  }
}

object ServerActor {
  def props(controller: ActorController): Props = Props(new ServerActor(controller))
}