package it.unibo.pps1920.motoscala.multiplayer.messages

import akka.actor.ActorRef
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandableEvent, DisplayableEvent}
import it.unibo.pps1920.motoscala.model.{MatchSetup, MultiPlayerSetup}

sealed trait Message
object DataType{
  type LobbyData = MessageData.LobbyData

}


object Message {
  import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
  case class PlainMessage(
    text: String,
    num: Int
  ) extends Message

  /*SERVER to client MESSAGES*/
  /*connection messages*/
  case class JoinResponseMessage(response: Boolean) extends Message

  /*in game messages*/
  case class LobbyDataMessage(lobbyData: LobbyData) extends Message
  case class GameStartMessage() extends Message
  case class GameEndMessage() extends Message
  case class DisplayMessage(event: DisplayableEvent)


  /*CLIENT to server MESSAGES*/
  /*connection messages*/
  case class JoinRequestMessage() extends Message
  case class ReadyMessage() extends Message

  /*in game messages*/
  case class CommandMessage(event:CommandableEvent) extends Message
}

object MessageData{
  final case class LobbyData(difficulty : Int, mode : Boolean, readyPlayers: Map[ActorRef, Boolean])
}