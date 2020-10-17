package it.unibo.pps1920.motoscala.model

import akka.actor.ActorRef
import it.unibo.pps1920.motoscala.multiplayer.messages.ErrorMsg.ErrorReason

import scala.collection.mutable
trait MatchSetup {
}


case class MultiPlayerSetup(var difficulty: Int = 0, var mode: Boolean = false,
                            var numPlayers: Int = 4) extends MatchSetup {
  var readyPlayers: mutable.Map[ActorRef, PlayerData] = mutable.LinkedHashMap()
  def setPlayerStatus(playerRef: ActorRef, isReady: Boolean): Unit = if (readyPlayers.contains(playerRef))
    readyPlayers.update(playerRef, PlayerData(readyPlayers(playerRef).name, status = isReady))
  def numReadyPlayers(): Int = readyPlayers.count(rp => rp._2.status)
  def tryAddPlayer(playerRef: ActorRef, name: String): Option[ErrorReason] = {
    if (readyPlayers.values.exists(playerData => playerData.name == name)) {
      return Some(ErrorReason("Name already taken", "Choose another name"))
    } else if (readyPlayers.size < numPlayers) {
      readyPlayers += (playerRef -> PlayerData(name, status = false))
      return None
    }
    return Some(ErrorReason("Lobby full", "No more slots available"))
  }
}

case class PlayerData(var name: String, var status: Boolean)

case class SinglePlayerSetup(difficulty: Int) extends MatchSetup
