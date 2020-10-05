package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.util.Direction
import it.unibo.pps1920.motoscala.ecs.util.Direction.Center
/**
 *
 * @param mass determines how far the entity will bounce away
 * @param duration keeps track of how long an active collision will last
 * @param direction direction the collision is causing the entity to go
 */
final case class CollisionComponent(mass: Int, var duration: Int = 0,
                                    var direction: Direction = Center) extends Component