package it.unibo.pps1920.motoscala.multiplayer.messages

import akka.actor.ActorRef
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandableEvent, DisplayableEvent}
import it.unibo.pps1920.motoscala.model.PlayerData
import it.unibo.pps1920.motoscala.multiplayer.messages.ErrorMsg.ErrorReason

import scala.collection.mutable

sealed trait Message
object DataType {
  type LobbyData = MessageData.LobbyData

}


object Message {
  import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
  case class PlainMessage(
    text: String,
    num: Int
  ) extends Message

  /*SERVER to client MESSAGES*/


  /*in game messages*/
  case class LobbyDataMessage(lobbyData: LobbyData) extends Message
  case class GameStartMessage() extends Message
  case class GameEndMessage() extends Message
  case class DisplayMessage(event: DisplayableEvent)


  /*CLIENT to server MESSAGES*/

  /*Client to Server connection messages*/
  case class JoinRequestMessage(name: String) extends Message
  case class JoinResponseMessage(option: Option[ErrorReason] = None) extends Message
  /*Also used from Controller to Client*/
  case class ReadyMessage(status: Boolean) extends Message

  /*Internal Controller to Client message*/
  case class TryJoin(serverSelection: String, name: String) extends Message
  /*Internal Timeout message*/
  case class TimeOut() extends Message

  /*in game messages*/
  case class CommandMessage(event: CommandableEvent) extends Message
}

object MessageData {
  final case class LobbyData(difficulty: Option[Int] = None, mode: Option[Boolean] = None,
                             readyPlayers: mutable.Map[ActorRef, PlayerData])
}


object ErrorMsg {
  case class ErrorReason(title: String, text: String)

}


