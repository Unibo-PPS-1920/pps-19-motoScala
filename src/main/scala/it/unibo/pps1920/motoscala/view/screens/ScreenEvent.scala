package it.unibo.pps1920.motoscala.view.screens

private[view] trait ScreenEvent
private[view] object ScreenEvent {
  final case object GoBack extends ScreenEvent
  final case object GoNext extends ScreenEvent
  final case object GotoGame extends ScreenEvent
  final case object GotoLevels extends ScreenEvent
  final case object GotoLobby extends ScreenEvent
  final case object GotoEnd extends ScreenEvent
  final case object GotoSelection extends ScreenEvent
  final case object GotoSettings extends ScreenEvent
  final case object GotoStats extends ScreenEvent
  final case object GotoHome extends ScreenEvent
}
