package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.components.Shape.Shape
import scalafx.scene.paint.Color

case class ShapeComponent(shape: Shape, color: Color) extends Component
