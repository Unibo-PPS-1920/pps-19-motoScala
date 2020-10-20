package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.util.Vector2

/**
 * Possible powerups and their effects
 */

object PowerUpEffect {
  sealed trait PowerUp {
    var duration: Int
    var isActive: Boolean
  }
  /**
   * @param duration how many ticks the powerup will affect the entity
   * @param modifier transformation of the affected component
   */
  case class SpeedBoostPowerUp(override var duration: Int, override var isActive: Boolean = false,
                               modifier: Vector2 => Vector2,
                               var oldVel: Vector2 = (0, 0)) extends PowerUp
  case class WeightBoostPowerUp(override var duration: Int, override var isActive: Boolean = false,
                                modifier: Double => Double,
                                var oldMass: Double = 0) extends PowerUp
  case class JumpPowerUp(override var duration: Int, override var isActive: Boolean = false) extends PowerUp
}