package it.unibo.pps1920.motoscala.multiplayer.messages

import akka.actor.ActorRef
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent
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
  /** This message means that the game is end. */
  final case class GameEndActorMessage() extends ActorMessage
  /** This message contains one media to be played. */
  final case class PlayMediaMessage(md: MediaEvent) extends ActorMessage

  /** This message carry the new state of the world, computed by the server,
   * it is used by the view for display the new world to user.
   *
   * @param event the event to display
   */
  final case class DisplayableActorMessage(event: DisplayableEvent) extends ActorMessage
  /** This message carry the command from the Client to the Server, the command is used by the server for computate the
   * new world and return a [[DisplayableActorMessage]].
   *
   * @param event the command to tell
   */
  final case class CommandableActorMessage(event: CommandableEvent) extends ActorMessage

  /*in lobby messages*/

  /** This message contains the data updated data of the lobby.
   *
   * @param lobbyData the new data
   */
  final case class LobbyDataActorMessage(lobbyData: LobbyData) extends ActorMessage
  /** This message tell to one client that server want to kick it out.
   *
   * @param ref the client ref
   */
  final case class KickActorMessage(ref: ActorRef) extends ActorMessage
  /** This message carry the result of the join request.
   *
   * @param option if None the request is accepted, otherwise the message contain the reason
   */
  final case class JoinResponseActorMessage(option: Option[ErrorReason] = None) extends ActorMessage
  /** Used to tell to clients that Server the server has close the lobby. */
  final case class CloseLobbyActorMessage() extends ActorMessage
  /** Used to set up the view for client.
   *
   * @param levelData contains all data needed for client to setup
   */
  final case class LevelSetupMessage(levelData: LevelSetupData) extends ActorMessage


  /*Client to Server messages*/

  /** Used to tell to server that client want to leave lobby.
   *
   * @param ref used for
   * */
  final case class LeaveEvent(ref: ActorRef) extends ActorMessage


  /** Used to ask if is possible to join in one lobby whit that name.
   *
   * @param name name of the client
   * */
  final case class JoinRequestActorMessage(name: String) extends ActorMessage


  /*Both used*/

  /** Used to deliver the sender status (ready or not) to the receiver.
   *
   * @param status the new status
   */
  final case class ReadyActorMessage(status: Boolean) extends ActorMessage
  /** Used to inform server and client that the game is now started. */
  final case class GameStartActorMessage() extends ActorMessage


  /*Used only from controller to Client*/

  /** Used to ask if is possible to join in one lobby whit that name, and provide client whit server selection for
   * the purpose.
   *
   * @param serverSelection server actor selection addr
   * @param name name of the client
   * */
  final case class TryJoin(serverSelection: String, name: String) extends ActorMessage

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


