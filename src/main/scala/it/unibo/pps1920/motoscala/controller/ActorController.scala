package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.model.MultiPlayerSetup
import it.unibo.pps1920.motoscala.view.ObserverUI

trait ActorController {


  import it.unibo.pps1920.motoscala.controller.mediation.Mediator
  import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
  def sendToViewStrategy(strategy: ObserverUI => Unit): Unit
  def sendToLobbyStrategy[T](strategy: MultiPlayerSetup => T): T
  def mediator: Mediator
  def getLobbyData: LobbyData
  def gameStart(): Unit
  def shutdownMultiplayer(): Unit
  def gameEnd(): Unit
  def gotKicked(): Unit
  def joinResult(result: Boolean): Unit
}
