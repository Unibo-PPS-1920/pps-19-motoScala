package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component

/**
 * Component for describing the physical sizes and color of an entity
 *
 * @param shape entity's shape
 * */
final case class ShapeComponent(shape: Shape) extends Component

