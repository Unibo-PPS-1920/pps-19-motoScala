package it.unibo.pps1920.motoscala.view.utilities

import it.unibo.pps1920.motoscala.view.screens.FXMLScreens._
import it.unibo.pps1920.motoscala.view.screens.ScreenEvent._
import it.unibo.pps1920.motoscala.view.screens.{FXMLScreens, ScreenEvent}
import org.driangle.sm.StateMachine

private[view] object ViewStateMachine {
  def buildStateMachine(): StateMachine[FXMLScreens, ScreenEvent] = StateMachine
    .WithFunctionTransitions[FXMLScreens, ScreenEvent]()
    .initialState(FXMLScreens.HOME)
    .transition({
      case (HOME, GotoGame) => GAME
      case (HOME, GotoLevels) => LEVELS
      case (HOME, GotoSelection) => SELECTION
      case (HOME, GotoSettings) => SETTINGS
      case (HOME, GotoStats) => STATS
      case (HOME, GotoHome) => HOME
      case (LEVELS, GoNext) => GAME
      case (SELECTION, GoBack) => HOME
      case (SELECTION, GotoLobby) => LOBBY
      case (GAME, GoBack) => LEVELS
      case (GAME, GotoEnd) => END
      case (END, GoBack) => HOME
      case (LEVELS, GoBack) => HOME
      case (LOBBY, GoBack) => HOME
      case (LOBBY, GotoGame) => GAME
      case (STATS, GoBack) => HOME
      case (SETTINGS, GoBack) => HOME
    }).build()
}
