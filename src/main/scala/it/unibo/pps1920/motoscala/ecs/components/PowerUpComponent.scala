package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.components.PowerUpEffect.PowerUp
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity

/**
 * Component for powerups
 *
 * @param entity player that will receive the powerup
 * @param effect effect of the powerup
 *
 */
final case class PowerUpComponent(
  var entity: Option[BumperCarEntity] = None, effect: PowerUp) extends Component


