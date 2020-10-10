package it.unibo.pps1920.motoscala.controller

trait ActorController {
  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.{Event, Mediator}
  import it.unibo.pps1920.motoscala.engine.Engine
  import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
  def getMediator : Mediator
  def getLobbyData : LobbyData
  def serverMode(gameEngine : Engine) : Unit
  def clientMode() : Unit
  //Called by Server Actor
  def requestJoin(ref : ActorRef) : Boolean
  def setReadyClient(ref: ActorRef)  : Unit
  //Called by Client Actor
  def gameStart() : Unit
  def gameEnd() : Unit
  def settingsUpdate(lobbyData : LobbyData) : Unit



}
