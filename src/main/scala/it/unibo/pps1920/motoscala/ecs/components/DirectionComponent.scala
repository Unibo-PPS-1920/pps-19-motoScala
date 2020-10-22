package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.util.Direction

/**
 * Component for direction
 *
 * @param dir direction the entity is pointing to
 */
final case class DirectionComponent(var dir: Direction) extends Component
