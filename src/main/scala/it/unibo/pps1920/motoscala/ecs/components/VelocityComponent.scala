package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component

/**
 * Component for velocity
 *
 * @param vel the velocity of the entity
 */
final case class VelocityComponent(var vel: Double) extends Component
