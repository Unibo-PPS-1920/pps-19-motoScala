package it.unibo.pps1920.motoscala.model

object Settings {
  sealed trait Settings
  case class SettingsData(volume: Float = 1.0f, diff: Int = 1, name: String = "Player") extends Settings

}

