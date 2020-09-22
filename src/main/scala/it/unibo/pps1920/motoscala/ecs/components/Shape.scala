package it.unibo.pps1920.motoscala.ecs.components

object Shape {

  sealed trait Shape

  case class Rectangle(center: (Float, Float), dimX: Float, dimY: Float) extends Shape

  case class Circle(center: (Float, Float), radius: Float) extends Shape

}