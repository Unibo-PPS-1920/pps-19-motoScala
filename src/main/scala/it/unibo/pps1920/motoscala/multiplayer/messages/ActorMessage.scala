package it.unibo.pps1920.motoscala.multiplayer.messages

import akka.actor.ActorRef
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandableEvent, DisplayableEvent}
import it.unibo.pps1920.motoscala.model.MatchSetup.Data.PlayerData
import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
import it.unibo.pps1920.motoscala.multiplayer.messages.ErrorMsg.ErrorReason
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LevelSetupData

import scala.collection.mutable

/** Represent a message used to by akka actors. */
sealed trait ActorMessage

/** Types used in design. */
object DataType {
  type LobbyData = MessageData.LobbyData
}

/** Represent a message used to by akka actors. */
object ActorMessage {


  /*SERVER to client MESSAGES*/

  /** Contain the setup for the view of the receiver client.
   *
   * @param setups all the necessary data for setup view
   */
  final case class SetupsForClientsMessage(setups: List[LevelSetupData])

  /*Server to Client messages*/

  /*in game messages*/
  /** */
  final case class GameEndActorMessage() extends ActorMessage
  final case class DisplayableActorMessage(event: DisplayableEvent) extends ActorMessage
  final case class CommandableActorMessage(event: CommandableEvent) extends ActorMessage

  /*in lobby messages*/

  final case class LobbyDataActorMessage(lobbyData: LobbyData) extends ActorMessage
  final case class KickActorMessage(ref: ActorRef) extends ActorMessage
  final case class JoinResponseActorMessage(option: Option[ErrorReason] = None) extends ActorMessage
  final case class CloseLobbyActorMessage() extends ActorMessage
  final case class LevelSetupMessage(levelData: LevelSetupData) extends ActorMessage


  /*Client to Server messages*/
  final case class LeaveEvent(ref: ActorRef) extends ActorMessage
  final case class TryJoin(serverSelection: String, name: String) extends ActorMessage
  final case class JoinRequestActorMessage(name: String) extends ActorMessage


  //*Both used*//
  final case class ReadyActorMessage(status: Boolean) extends ActorMessage
  final case class GameStartActorMessage() extends ActorMessage


  /*Self sent message*/

  /*Internal Timeout message, sent by internal general purpose Timeout*/
  final case class TimeOut() extends ActorMessage


}

object MessageData {
  /** Contains the lobby data, the data can be None, where None means no difference between previous data.
   *
   * @param difficulty the difficult levl None if the same as previous
   * @param level the level index None if the same as previous
   * @param readyPlayers the lobby players status with name, None if the same as previous
   */
  final case class LobbyData(
    difficulty: Option[Int] = None, level: Option[Int] = None, readyPlayers: mutable.Map[ActorRef, PlayerData])
}

object ErrorMsg {
  /** Contains the error of the join request result.
   *
   * @param title error title
   * @param text error body
   */
  final case class ErrorReason(title: String, text: String)
}


