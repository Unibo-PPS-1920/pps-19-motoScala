package it.unibo.pps1920.motoscala.ecs.components

object Shape {

  sealed trait Shape

  case object Square extends Shape

  case object Circle extends Shape

}