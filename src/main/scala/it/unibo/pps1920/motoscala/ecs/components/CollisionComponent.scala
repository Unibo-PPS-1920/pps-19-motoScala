package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.util.Direction
/**
 * Component for collisions
 */
final case class CollisionComponent(var duration: Int, var direction: Direction) extends Component