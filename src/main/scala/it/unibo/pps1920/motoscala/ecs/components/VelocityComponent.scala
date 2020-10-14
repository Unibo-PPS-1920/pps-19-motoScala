package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.util.Vector2

/**
 * Component for velocity
 *
 * @param currentVel the velocity of the entity
 */
final case class VelocityComponent(var currentVel: Vector2, var defVel: Vector2 = Vector2(20, 20),
                                   var inputVel: Vector2 = Vector2(0, 0)) extends Component
