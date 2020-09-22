package it.unibo.pps1920.motoscala.view

object FXMLScreens {
  sealed abstract class FXMLScreens(val resourcePath: String, val cssPath: String)
  final case object HOME extends FXMLScreens("/screens/Home.fxml", "/sheets/Home.css")
  final case object GAME extends FXMLScreens("/screens/Game.fxml", "/sheets/Game.css")
  final case object LOBBY extends FXMLScreens("/screens/Lobby.fxml", "/sheets/Lobby.css")
  final case object SETTINGS extends FXMLScreens("/screens/Settings.fxml", "/sheets/Settings.css")
  final case object STATS extends FXMLScreens("/screens/Stats.fxml", "/sheets/Stats.css")
}
