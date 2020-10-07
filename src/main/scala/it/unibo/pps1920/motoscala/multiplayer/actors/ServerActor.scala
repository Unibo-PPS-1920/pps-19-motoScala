package it.unibo.pps1920.motoscala.multiplayer.actors
import it.unibo.pps1920.motoscala.multiplayer.messages._
import akka.actor.{Actor, ActorLogging}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.multiplayer.messages.PlainMessage
class ServerActor (protected val actorController: ActorController) extends Actor with ActorLogging{
  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.Event.DisplayableEvent
  import it.unibo.pps1920.motoscala.controller.mediation.Mediator
  val controller : ActorController = actorController
  val levelMediator : Mediator = controller.getMediator
  var clients : Set[ActorRef] = Set()
  def handle(displayEvent : DisplayableEvent): Unit = clients.foreach(c=> c.tell(DisplayMessage(displayEvent), this.self))
  override def receive: Receive = {

    case PlainMessage(text, num) => println("TESTO: "+text + "NUM:" + num)
    case CommandMessage(event) => controller.notifyUsersUpdate(event)
    case JoinRequestMessage => controller.notifyJoinRequest(this.sender())
    case ReadyMessage => controller.notifyReady(this.sender())
    case _ => println("AAAAAAA "+akka.serialization.Serialization.serializedActorPath(self))
  }
}

object ServerActor {
  import akka.actor.Props
  def props(controller: ActorController): Props = Props(new ServerActor(controller))
}