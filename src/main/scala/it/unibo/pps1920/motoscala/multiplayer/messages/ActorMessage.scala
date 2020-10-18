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
  import it.unibo.pps1920.motoscala.model.Level.LevelData
  import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
  case class PlainActorMessage(
    text: String,
    num: Int
  ) extends ActorMessage

  /*SERVER to client MESSAGES*/


  /*in game messages*/
  final case class LobbyDataActorMessage(lobbyData: LobbyData) extends ActorMessage
  final case class GameStartActorMessage() extends ActorMessage
  final case class GameEndActorMessage() extends ActorMessage
  final case class DisplayableActorMessage(event: DisplayableEvent) extends ActorMessage


  /*CLIENT to server MESSAGES*/

  /*Server to Client messages*/
  final case class KickActorMessage(ref: ActorRef) extends ActorMessage
  final case class JoinResponseActorMessage(option: Option[ErrorReason] = None) extends ActorMessage
  final case class CloseLobbyActorMessage() extends ActorMessage
  final case class LeaveEvent(ref: ActorRef) extends ActorMessage

  /*Client to Server connection messages*/
  final case class JoinRequestActorMessage(name: String) extends ActorMessage
  /*Also used from Controller to Client*/
  final case class ReadyActorMessage(status: Boolean) extends ActorMessage

  /*Internal Controller to Client message*/
  final case class TryJoin(serverSelection: String, name: String) extends ActorMessage
  /*Internal Timeout message*/
  final case class TimeOut() extends ActorMessage

  /*in game messages*/
  final case class CommandableActorMessage(event: CommandableEvent) extends ActorMessage
}

object MessageData {
  final case class LobbyData(difficulty: Option[Int] = None, mode: Option[Boolean] = None,
                             readyPlayers: mutable.Map[ActorRef, PlayerData])
}


object ErrorMsg {
  case class ErrorReason(title: String, text: String)

}


