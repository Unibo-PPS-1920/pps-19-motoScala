package it.unibo.pps1920.motoscala.view.utilities

import it.unibo.pps1920.motoscala.view.screens.{FXMLScreens, ScreenEvent}
import org.driangle.sm.StateMachine

private[view] object ViewStateMachine {
  def buildStateMachine(): StateMachine[FXMLScreens, ScreenEvent] = StateMachine
    .WithFunctionTransitions[FXMLScreens, ScreenEvent]()
    .initialState(FXMLScreens.HOME)
    .transition({
      case (HOME, GotoLevels) => LEVELS
      case (HOME, GotoLobby) => LOBBY
      case (HOME, GotoSettings) => SETTINGS
      case (HOME, GotoStats) => STATS
      case (LEVELS, GoNext) => GAME
      case (GAME, GoBack) => LEVELS
      case (LEVELS, GoBack) => HOME
      case (LOBBY, GoBack) => HOME
      case (STATS, GoBack) => HOME
      case (SETTINGS, GoBack) => HOME
    })
    .build()
}
