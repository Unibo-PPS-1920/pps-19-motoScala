package it.unibo.pps1920.motoscala.multiplayer.messages

import akka.actor.ActorRef
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandableEvent, DisplayableEvent}
import it.unibo.pps1920.motoscala.model.PlayerData
import it.unibo.pps1920.motoscala.multiplayer.messages.ErrorMsg.ErrorReason

import scala.collection.mutable

sealed trait ActorMessage
object DataType {
  type LobbyData = MessageData.LobbyData

}


object ActorMessage {
  import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
  case class PlainActorMessage(
    text: String,
    num: Int
  ) extends ActorMessage

  /*SERVER to client MESSAGES*/


  /*in game messages*/
  case class LobbyDataActorMessage(lobbyData: LobbyData) extends ActorMessage
  case class GameStartActorMessage() extends ActorMessage
  case class GameEndActorMessage() extends ActorMessage
  case class DisplayActorMessage(event: DisplayableEvent)


  /*CLIENT to server MESSAGES*/

  /*Server to Client messages*/
  case class KickActorMessage(ref: ActorRef) extends ActorMessage
  case class JoinResponseActorMessage(option: Option[ErrorReason] = None) extends ActorMessage
  final case class CloseLobby() extends ActorMessage


  /*Client to Server connection messages*/
  case class JoinRequestActorMessage(name: String) extends ActorMessage
  /*Also used from Controller to Client*/
  case class ReadyActorMessage(status: Boolean) extends ActorMessage

  /*Internal Controller to Client message*/
  case class TryJoin(serverSelection: String, name: String) extends ActorMessage
  /*Internal Timeout message*/
  case class TimeOut() extends ActorMessage

  /*in game messages*/
  case class CommandActorMessage(event: CommandableEvent) extends ActorMessage
}

object MessageData {
  final case class LobbyData(difficulty: Option[Int] = None, mode: Option[Boolean] = None,
                             readyPlayers: mutable.Map[ActorRef, PlayerData])
}


object ErrorMsg {
  case class ErrorReason(title: String, text: String)

}


