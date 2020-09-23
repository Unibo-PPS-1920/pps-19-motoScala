package it.unibo.pps1920.motoscala.view.events

sealed trait ViewEvent
object ViewEvent {
  sealed trait HomeEvent extends ViewEvent
  sealed trait GameEvent extends ViewEvent
  sealed trait LobbyEvent extends ViewEvent
  sealed trait SettingsEvent extends ViewEvent
  sealed trait StatsEvent extends ViewEvent
}
