package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component

/**
 * Component for powerups
 *
 * @param value effect of the powerup
 */
final case class PowerUpComponent(var value: Int) extends Component

