package it.unibo.pps1920.motoscala.multiplayer.actors

import akka.actor.{Actor, ActorLogging}

/** Represent one actor whit hidden logging feature,
 * Akka logging and internal logging provided in clean way via implicit.
 */
protected[multiplayer] abstract class ActorWhitLogging extends Actor with ActorLogging {
  /** The logger used, need to be initialize to the concrete subclass */
  protected val logger: org.slf4j.Logger
  /** This implicit provide a clean way to avoid providing all the changeBehaviour, whit one context to do become */
  protected implicit val getContext: akka.actor.ActorContext = this.context
  /** Extends the partial function Actor Behaviour whit another exhaustive Behaviour that log all strange message encountered.
   *
   * @param behaviour The partial function behaviour to be extended
   * @param context The implicit context provider
   */
  protected def changeBehaviourWhitLogging(behaviour: Actor.Receive)(implicit context: akka.actor.ActorContext): Unit =
    context.become(behaviour orElse unexpectedBehaviour)

  private def unexpectedBehaviour: Actor.Receive = {case msg => unexpectedMessageHandler(msg)}

  private def unexpectedMessageHandler(msg: Any): Unit = logger warn s"Received unexpected message $msg"
}
