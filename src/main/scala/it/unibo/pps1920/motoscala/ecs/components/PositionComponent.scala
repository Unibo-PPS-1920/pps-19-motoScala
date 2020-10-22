package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.util.Vector2

/**
 * Component for describing position
 *
 * @param pos entity's position on the plane
 */

final case class PositionComponent(var pos: Vector2) extends Component
