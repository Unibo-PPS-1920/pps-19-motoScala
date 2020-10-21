package it.unibo.pps1920.motoscala.view.fsm

/** Event used to change screen. This is processed by the Final State Machine */
private[view] trait ChangeScreenEvent
private[view] object ChangeScreenEvent {
  final case object GoBack extends ChangeScreenEvent
  final case object GoNext extends ChangeScreenEvent
  final case object GotoGame extends ChangeScreenEvent
  final case object GotoLevels extends ChangeScreenEvent
  final case object GotoLobby extends ChangeScreenEvent
  final case object GotoSelection extends ChangeScreenEvent
  final case object GotoSettings extends ChangeScreenEvent
  final case object GotoStats extends ChangeScreenEvent
  final case object GotoHome extends ChangeScreenEvent
}
