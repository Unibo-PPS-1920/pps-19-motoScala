package it.unibo.pps1920.motoscala.controller

trait ActorController {
  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.{Event, Mediator}
  import it.unibo.pps1920.motoscala.engine.Engine
  import it.unibo.pps1920.motoscala.model.MatchSetup

  def notifySettingsUpdate(setup : MatchSetup) : Unit
  def notifyJoinRequest(ref : ActorRef) : Boolean
  def notifyUsersUpdate(event : Event) : Unit
  def notifyNewWorld(event: Event) : Unit
  def notifyGameStart() : Unit
  def notifyGameEnd() : Unit
  def notifyReady(ref: ActorRef)  : Unit
  def getMediator : Mediator
  def serverMode(gameEngine : Engine) : Unit
  def clientMode() : Unit
}
