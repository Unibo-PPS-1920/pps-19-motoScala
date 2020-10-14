package it.unibo.pps1920.motoscala.model

import akka.actor.ActorRef

import scala.collection.mutable
trait MatchSetup {
}


case class MultiPlayerSetup(var difficulty: Int = 0, var mode: Boolean = false,
                            var numPlayers: Int = 4) extends MatchSetup {
  var readyPlayers: mutable.Map[ActorRef, PlayerData] = mutable.LinkedHashMap()
  def setPlayerStatus(playerRef: ActorRef, isReady: Boolean): Unit = if (readyPlayers.contains(playerRef))
    readyPlayers.update(playerRef, PlayerData(readyPlayers(playerRef).name, status = isReady))
  def numReadyPlayers(): Int = readyPlayers.count(rp => rp._2.status)
  def tryAddPlayer(playerRef: ActorRef, name: String): Boolean = {
    if (readyPlayers.size < numPlayers) {readyPlayers += (playerRef -> PlayerData(name, status = false)); true }
    else false
  }
}

case class PlayerData(var name: String, var status: Boolean)

case class SinglePlayerSetup(difficulty: Int) extends MatchSetup
