package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging}

protected[multiplayer] abstract class ActorWhitLogging extends Actor with ActorLogging {
  protected val logger: org.slf4j.Logger

  protected implicit val getContext: akka.actor.ActorContext = this.context

  protected def changeBehaviourWhitLogging(behaviour: Actor.Receive)(implicit context: akka.actor.ActorContext): Unit =
    context.become(behaviour orElse unexpectedBehaviour)

  private def unexpectedBehaviour: Actor.Receive = {case msg => unexpectedMessageHandler(msg)}

  private def unexpectedMessageHandler(msg: Any): Unit = logger warn s"Received unexpected message $msg"
}
