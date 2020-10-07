package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.util.Vector2

/**
 * Component for velocity
 *
 * @param vel the velocity of the entity
 */
final case class VelocityComponent(var vel: Vector2) extends Component
