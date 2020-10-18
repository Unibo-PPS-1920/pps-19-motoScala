package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.util.Vector2

/**
 * Possible powerups and their effects
 */

object PowerUpEffect {
  sealed trait PowerUp {var duration: Int}
  /**
   * @param duration how many ticks the powerup will affect the entity
   * @param modifier transformation of the affected component
   */

  case class SpeedBoostPowerUp(override var duration: Int, modifier: Vector2 => Vector2) extends PowerUp
  case class WeightBoostPowerUp(override var duration: Int, modifier: Double => Double,
                                var oldMass: Double = 0) extends PowerUp
  case class JumpPowerUp(override var duration: Int) extends PowerUp
}