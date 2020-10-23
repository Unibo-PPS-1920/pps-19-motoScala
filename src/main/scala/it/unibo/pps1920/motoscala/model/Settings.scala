package it.unibo.pps1920.motoscala.model

/** Represent the settings of the program, like sound volume, difficult ecc... */
sealed trait Settings

/** Provide default settings data instance. */
object Settings {
  /** A classic level settings.
   *
   * @param musicVolume the music volume
   * @param effectVolume the effect volume
   * @param diff the difficult
   * @param name the of the player
   */
  case class SettingsData(
    musicVolume: Float = 0.5f, effectVolume: Float = 1.0f, diff: Int = Difficulties.EASY.number,
    name: String = "Player") extends Settings

}

