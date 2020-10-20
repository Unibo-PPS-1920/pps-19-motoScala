package it.unibo.pps1920.motoscala.model

object Settings {
  sealed trait Settings
  case class SettingsData(musicVolume: Float = 0.5f, effectVolume: Float = 1.05f, diff: Int = 1,
                          name: String = "Player") extends Settings

}

