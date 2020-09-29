package it.unibo.pps1920.motoscala.ecs.components

/**
 * Possible shapes
 */
object Shape {
  sealed trait Shape
  case class Rectangle(dimX: Float, dimY: Float) extends Shape
  case class Circle(radius: Float) extends Shape
}