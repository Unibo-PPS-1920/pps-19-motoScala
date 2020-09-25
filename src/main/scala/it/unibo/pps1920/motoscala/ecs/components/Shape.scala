package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.util.Vector2

/**
 * Possible shapes
 */
object Shape {


  sealed trait Shape

  case class Rectangle(center: Vector2, dimX: Float, dimY: Float) extends Shape

  case class Circle(center: Vector2, radius: Float) extends Shape

}