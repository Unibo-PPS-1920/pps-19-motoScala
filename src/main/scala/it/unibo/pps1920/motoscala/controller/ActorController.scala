package it.unibo.pps1920.motoscala.controller

trait ActorController {
  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.Mediator
  import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
  def getMediator: Mediator
  def getLobbyData: LobbyData
  //Called by Server Actor
  def requestJoin(ref: ActorRef): Boolean
  def setReadyClient(ref: ActorRef): Unit
  //Called by Client Actor
  def gameStart(): Unit
  def gameEnd(): Unit
  def settingsUpdate(lobbyData: LobbyData): Unit


}
