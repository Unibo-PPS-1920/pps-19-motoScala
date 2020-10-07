package it.unibo.pps1920.motoscala.actors.messages

import it.unibo.pps1920.motoscala.controller.mediation.Event.DisplayableEvent

sealed trait Message
object DataType{
  type LobbyData

}
case class PlainMessage(
  text: String,
  num: Int
) extends Message

/*SERVER to client MESSAGES*/
/*connection messages*/
case class JoinResponseMessage(
  response: Boolean
) extends Message

/*in game messages*/
case class LobbyDataMessage(data : LobbyDataMessage) extends Message
case class GameStartMessage() extends Message
case class GameEndMessage() extends Message

/*CLIENT to server MESSAGES*/
/*connection messages*/
case class JoinRequestMessage() extends Message
case class ReadyMessage() extends Message

/*in game messages*/
