package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.util.Direction
/**
 *
 * @param mass determines how far the entity will bounce away
 * @param duration keeps track of how long an active collision will last
 * @param colDirection direction the collision is causing the entity to go
 * @param oldSpeed original speed to be restored after collision has happened
 */
final case class CollisionComponent(var mass: Double,
                                    var duration: Int = 0,
                                    var colDirection: Direction,
                                    var inDirection: Direction,
                                    var oldSpeed: Double) extends Component