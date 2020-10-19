package it.unibo.pps1920.motoscala.view.events

import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.model.Scores.ScoresData
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
import it.unibo.pps1920.motoscala.view

sealed trait ViewEvent
object ViewEvent {

  import it.unibo.pps1920.motoscala.controller.mediation.EventData.LevelSetupData
  import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
  sealed trait HomeEvent extends ViewEvent
  sealed trait LevelEvent extends ViewEvent
  sealed trait GameEvent extends ViewEvent
  sealed trait LobbyEvent extends ViewEvent
  sealed trait SelectionEvent extends ViewEvent
  sealed trait GlobalViewEvent extends ViewEvent
  sealed trait SettingsEvent extends ViewEvent
  sealed trait StatsEvent extends ViewEvent
  final case class LevelSetupEvent(data: LevelSetupData) extends GameEvent
  final case class ShowDialogEvent(title: String, msg: String, secondsDuration: view.JavafxEnums.Notification_Duration,
                                   notificationType: view.JavafxEnums.NotificationType) extends GlobalViewEvent
  final case class JoinResultEvent(result: Boolean) extends SelectionEvent
  final case class LobbyDataEvent(lobbyData: LobbyData) extends LobbyEvent
  final case class LeaveLobbyEvent() extends LobbyEvent
  final case class LoadLevelEvent() extends LobbyEvent
  final case class SetupLobbyEvent(ip: String, port: String, name: String) extends LobbyEvent
  final case class LevelDataEvent(levels: Seq[LevelData]) extends LevelEvent
  final case class SettingsDataEvent(settings: SettingsData) extends SettingsEvent
  final case class ScoreDataEvent(scores: ScoresData) extends StatsEvent

}
