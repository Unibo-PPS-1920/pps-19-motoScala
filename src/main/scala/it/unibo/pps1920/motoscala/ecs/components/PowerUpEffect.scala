package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.util.Vector2


object PowerUpEffect {
  sealed trait PowerUp {var duration: Int}

  case class SpeedBoostPowerUp(override var duration: Int, modifier: Vector2 => Vector2) extends PowerUp
  case class WeightBoostPowerUp(override var duration: Int, modifier: Double => Double,
                                var oldMass: Double = 0) extends PowerUp
  case class JumpPowerUp(override var duration: Int) extends PowerUp
}