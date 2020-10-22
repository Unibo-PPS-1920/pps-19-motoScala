package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
import it.unibo.pps1920.motoscala.view.ObserverUI

/** The Controller side of the Observer, the [[it.unibo.pps1920.motoscala.view.ObserverUI]] can observe the [[ObservableUI]].
 * Expose all the function that the view can call to the Controller, it expose even some useful getter. */
trait ObservableUI extends SoundController {
  /** The getter of actual selected level. */
  type Level = Int
  /** The getter of the player score. */
  var score: Int
  /** Initialize the Server actor, and open a new lobby. */
  def becomeHost(): Unit
  /** Try to join into one lobby, using the the ip and port.
   *
   * @param ip the ip to contact
   * @param port the port of the server actor
   * */
  def tryJoinLobby(ip: String, port: String): Unit
  /** Kick one player from the lobby and communicate to the client whit that name the kick.
   *
   * @param name the name of the client tobe kicked
   * */
  def kickSomeone(name: String): Unit
  /** Contains all the changed information inside the lobby.
   *
   * @param level the levels, None if not changed
   * @param difficult the difficult, None if not changed
   * @param isStatusChanged the status of the sender, true if is ready, false otherwise
   * */
  def lobbyInfoChanged(level: Option[Int] = None, difficult: Option[Int] = None, isStatusChanged: Boolean = false): Unit
  /** Provide te possibility to shutdown the multiplayer akka server and client. */
  def shutdownMultiplayer(): Unit
  /** Used for leaving the lobby, when kicked. */
  def leaveLobby(): Unit
  /** The Controller Observer. Part of the Observer Pattern
   * Called when some observer want to start watch this [[ObservableUI]].
   *
   * @param obs the list of observer to be added
   */
  def attachUI(obs: ObserverUI*): Unit
  /** The Controller Observer. Part of the Observer Pattern
   * it is used for stop observe one observable [[it.unibo.pps1920.motoscala.controller.ObservableUI]]
   *
   * Called for remove a set of observer.
   *
   * @param obs the list of observer to be removed
   */
  def detachUI(obs: ObserverUI*): Unit
  /** Setup one game, load level, get player numbers, set up the data
   * and send all to clients if presents and server.
   *
   * @param level the level to be loaded
   */
  def setupGame(level: Level): Unit
  /** Mediator getter, provide the mediator for view instances, is useful for screes that use it for registers itself
   * to new coming events.
   *
   * @return the [[it.unibo.pps1920.motoscala.controller.mediation.Mediator]]
   * */
  def mediator: Mediator
  /** Deserialize and load all Levels. */
  def loadAllLevels(): Unit
  /** Deserialize and load Settings. */
  def loadSetting(): Unit
  /** Deserialize and load Stats. */
  def loadStats(): Unit
  /** Serialize and save the new Settings.
   *
   * @param newSettings the settings to be serialize.
   * */
  def saveSettings(newSettings: SettingsData): Unit
  /** Starts the multiplayer game for all, server and clients. */
  def startMultiplayerGame(): Unit
  /** Starts the [[it.unibo.pps1920.motoscala.engine.Engine]]. */
  def start(): Unit
  /** Pause the [[it.unibo.pps1920.motoscala.engine.Engine]]. */
  def pause(): Unit
  /** Resume the [[it.unibo.pps1920.motoscala.engine.Engine]]. */
  def resume(): Unit
  /** Stop the [[it.unibo.pps1920.motoscala.engine.Engine]]. */
  def stop(): Unit
  /** Serialize the stats if is a multiplayer match,and is a new record. */
  def saveStats(): Unit
}


