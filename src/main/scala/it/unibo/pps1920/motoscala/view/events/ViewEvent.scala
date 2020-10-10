package it.unibo.pps1920.motoscala.view.events

import it.unibo.pps1920.motoscala.model.Level.LevelData

sealed trait ViewEvent
object ViewEvent {
  import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
  sealed trait HomeEvent extends ViewEvent
  sealed trait LevelEvent extends ViewEvent
  final case class LevelDataEvent(levels: Seq[LevelData]) extends LevelEvent
  sealed trait GameEvent extends ViewEvent
  sealed trait LobbyEvent extends ViewEvent
  final case class LobbyDataEvent(lobbyData : LobbyData) extends  LobbyEvent
  sealed trait SettingsEvent extends ViewEvent
  sealed trait StatsEvent extends ViewEvent
}
