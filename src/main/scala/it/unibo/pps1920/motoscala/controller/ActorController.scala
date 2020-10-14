package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.model.MultiPlayerSetup
import it.unibo.pps1920.motoscala.view.ObserverUI

trait ActorController {


  import akka.actor.ActorRef
  import it.unibo.pps1920.motoscala.controller.mediation.Mediator
  import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
  def sendToViewStrategy(strategy: ObserverUI => Unit): Unit
  def sendToLobbyStrategy[T](strategy: MultiPlayerSetup => T): T
  def getMediator: Mediator
  def getLobbyData: LobbyData
  //Called by Server Actor
  def requestJoin(ref: ActorRef): Boolean
  def setReadyClient(ref: ActorRef): Unit
  //Called by Client Actor
  def gameStart(): Unit
  def gameEnd(): Unit
  def settingsUpdate(lobbyData: LobbyData): Unit
  def joinResult(result: Boolean): Unit


}
