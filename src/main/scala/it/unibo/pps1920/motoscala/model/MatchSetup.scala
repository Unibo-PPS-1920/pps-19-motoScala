package it.unibo.pps1920.motoscala.model
import akka.actor.ActorRef
trait MatchSetup {
}


case class MultiPlayerSetup(difficulty: Int, mode: Boolean, numPlayers: Int) extends MatchSetup {
  var readyPlayers : Map[ActorRef, Boolean] = Map()
  def setReady(playerRef:ActorRef): Unit = if(readyPlayers.contains(playerRef)) readyPlayers+=(playerRef -> true)
  def unsetReady(playerRef:ActorRef): Unit = readyPlayers+=(playerRef -> false)
  def numReadyPlayers() : Int = readyPlayers.count(rp=> rp._2)
  def tryAddPlayer(playerRef:ActorRef) : Boolean = {
    if(readyPlayers.size < numPlayers) {readyPlayers+=(playerRef->false); true}
    else false
  }
}

case class SinglePlayerSetup(difficulty: Int) extends MatchSetup