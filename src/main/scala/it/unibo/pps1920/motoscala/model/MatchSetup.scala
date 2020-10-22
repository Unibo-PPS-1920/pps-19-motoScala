package it.unibo.pps1920.motoscala.model

import akka.actor.ActorRef
import it.unibo.pps1920.motoscala.model.MatchSetup.Data.PlayerData
import it.unibo.pps1920.motoscala.multiplayer.messages.ErrorMsg.ErrorReason

import scala.collection.mutable
trait MatchSetup {
}

object MatchSetup {
  /** A [[MatchSetup]] represents the match setup.
   *
   * @param difficulty the difficulty of the match
   * @param numPlayers the match max player
   * @param level the match selected level
   */
  case class MultiPlayerSetup(var difficulty: Int = 1, var level: Int = 1, var numPlayers: Int = 4) extends MatchSetup {
    private var _readyPlayers: mutable.Map[ActorRef, PlayerData] = mutable.LinkedHashMap()

    import MagicValues._

    /** Set the status of a player, it can be true or false, where false means not ready.
     *
     * @param playerRef the player reference
     * @param isReady the palyer new status
     */
    def setPlayerStatus(playerRef: ActorRef, isReady: Boolean): Unit =
      if (_readyPlayers.contains(playerRef))
        _readyPlayers.update(playerRef, PlayerData(_readyPlayers(playerRef).name, status = isReady))
    /** Return a copy of the player map.
     *
     * @return the match map of present player
     * */
    def getPlayerStatus: mutable.Map[ActorRef, PlayerData] = _readyPlayers.clone()
    /** Return the number of ready player present inside the match. */
    def numReadyPlayers(): Int = _readyPlayers.count(rp => rp._2.status)
    /** Try to add one player, to the match, it can fail if the name already exist or if the lobby is full.
     *
     * @param playerRef the player reference
     * @param name the name of the new player
     * @return an option contains the error reason if presents
     */
    def tryAddPlayer(playerRef: ActorRef, name: String): Option[ErrorReason] = {
      if (_readyPlayers.values.exists(playerData => playerData.name == name))
        return Some(ErrorReason(TitleNameAlreadyTakenError, TextNameAlreadyTakenError))
      else if (_readyPlayers.size < numPlayers) {
        _readyPlayers += (playerRef -> PlayerData(name, status = false))
        return None
      }
      Some(ErrorReason(TitleLobbyFullTakenError, TextNamLobbyFullTakenError))
    }
    /** Remove one player from the match.
     *
     * @param name the name of the player to remove
     */
    def removePlayer(name: String): ActorRef = {
      val player = _readyPlayers.find(_._2.name == name).get
      _readyPlayers.remove(player._1)
      player._1
    }
    private object MagicValues {
      final val TitleNameAlreadyTakenError = "Name already taken"
      final val TextNameAlreadyTakenError = "Choose another name"
      final val TitleLobbyFullTakenError = "Lobby full"
      final val TextNamLobbyFullTakenError = "No more slots available"
    }
  }

  object Data {
    /** Represent the name and the status of one player.
     *
     * @param status the player status
     * @param name the player name
     */
    case class PlayerData(var name: String, var status: Boolean)
  }
}


