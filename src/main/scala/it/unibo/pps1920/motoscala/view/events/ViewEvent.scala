package it.unibo.pps1920.motoscala.view.events

import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.model.ReadyTable.ReadyPlayers
import it.unibo.pps1920.motoscala.model.Scores.ScoresData
import it.unibo.pps1920.motoscala.model.Settings.SettingsData

sealed trait ViewEvent
object ViewEvent {
  import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
  sealed trait HomeEvent extends ViewEvent
  sealed trait LevelEvent extends ViewEvent
  sealed trait GameEvent extends ViewEvent
  sealed trait LobbyEvent extends ViewEvent
  sealed trait SelectionEvent extends ViewEvent
  sealed trait SettingsEvent extends ViewEvent
  sealed trait StatsEvent extends ViewEvent
  final case class JoinResult(result: Boolean) extends SelectionEvent
  final case class LobbyDataEvent(lobbyData: LobbyData) extends LobbyEvent
  final case class UpdateReadyPlayer(playersStatus: ReadyPlayers) extends LobbyEvent
  final case class SetupLobby(ip: String, port: String) extends LobbyEvent
  final case class LevelDataEvent(levels: Seq[LevelData]) extends LevelEvent
  final case class SettingsDataEvent(settings: SettingsData) extends SettingsEvent
  final case class ScoreDataEvent(scores: ScoresData) extends StatsEvent

}
