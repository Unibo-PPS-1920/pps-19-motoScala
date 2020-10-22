package it.unibo.pps1920.motoscala.view.screens

/** Fxml screens constants */
private[view] sealed abstract class FXMLScreens(val resourcePath: String, val cssPath: String)
private[view] object FXMLScreens {
  final case object HOME extends FXMLScreens("/screens/Home.fxml", "/sheets/Home.css")
  final case object GAME extends FXMLScreens("/screens/Game.fxml", "/sheets/Game.css")
  final case object LEVELS extends FXMLScreens("/screens/Levels.fxml", "/sheets/Levels.css")
  final case object SELECTION extends FXMLScreens("/screens/ModeSelection.fxml", "/sheets/ModeSelection.css")
  final case object LOBBY extends FXMLScreens("/screens/Lobby.fxml", "/sheets/Lobby.css")
  final case object SETTINGS extends FXMLScreens("/screens/Settings.fxml", "/sheets/Settings.css")
  final case object STATS extends FXMLScreens("/screens/Stats.fxml", "/sheets/Stats.css")
}
