package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.components.Shape.Shape
import scalafx.scene.paint.Color

/**
 * Component for describing the physical sizes and color of an entity
 *
 * @param shape entity's shape
 * @param color entity's color
 */
case class ShapeComponent(shape: Shape, color: Color) extends Component
