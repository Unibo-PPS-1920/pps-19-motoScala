package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.model.MatchSetup.MultiPlayerSetup
import it.unibo.pps1920.motoscala.view.ObserverUI
/** The actor that is used for */
trait ActorController {


  import it.unibo.pps1920.motoscala.controller.mediation.Mediator
  import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
  /** One strategy used to send one action to view without passing trough controller.
   * Used for avoid redundant call, or spoiled call.
   *
   * @param strategy
   */
  def sendToViewStrategy(strategy: ObserverUI => Unit): Unit
  /**
   * One strategy used to send one action to the lobby without passing trough controller.
   * Used for avoid redundant call, or spoiled call.
   * It can be used for retrieving useful data.
   *
   * @param strategy
   * @tparam T
   * @return
   */
  def sendToLobbyStrategy[T](strategy: MultiPlayerSetup => T): T
  /** Mediator getter, provide the mediator for view instances, is useful for screes that use it for registers itself
   * to new coming events.
   *
   * @return the [[Mediator]]
   * */
  def mediator: Mediator
  /** Called to retrieve the actual lobby status.
   *
   * @return the actual status
   */
  def getLobbyData: LobbyData
  /** * Called when the game got started to the hosting server, change the view and prepare the client. */
  def gameStart(): Unit
  /** Provide te possibility to shutdown the multiplayer akka server and client. */
  def shutdownMultiplayer(): Unit
  /** Called when this player got kicked by server, it clean multiplayer status and provide useful message */
  def gotKicked(): Unit
  /**
   * Return the result of a previous request to join.
   *
   * @param result true if the request is accepted false otherwise
   */
  def joinResult(result: Boolean): Unit
}
