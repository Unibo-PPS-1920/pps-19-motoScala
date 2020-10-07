package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging}
import it.unibo.pps1920.motoscala.controller.ActorController
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.multiplayer.messages._
private class ClientActor(protected val actorController: ActorController) extends Actor with ActorLogging{
  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
  var serverActor : Option[ActorRef] = None
  val controller : ActorController = actorController
  val levelMediator : Mediator = controller.getMediator

  //def connectToServer() = ???
  def handle(commandEvent: CommandEvent): Unit = serverActor.get.tell(CommandMessage(commandEvent), this.self)

  override def receive: Receive = {
    case JoinResponseMessage(response==true) => ???
    case JoinResponseMessage(response == false) => ???
    case LobbyDataMessage(setup) => controller.notifySettingsUpdate(setup)
    case GameStartMessage => controller.notifyGameStart()
    case GameEndMessage => controller.notifyGameEnd()
    case DisplayMessage(event) => controller.notifyNewWorld(event)
    case _ => println("JKCHJWEOKFJWOPFJWOEFJKWPKFWPEKPWEOFKWPEOFKWPEFKWè")
  }
}

object ClientActor{
  import akka.actor.Props
  def props(controller: ActorController): Props = Props(new ClientActor(controller))
}