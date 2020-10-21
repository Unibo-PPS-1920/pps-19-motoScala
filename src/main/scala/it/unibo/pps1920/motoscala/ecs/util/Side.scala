package it.unibo.pps1920.motoscala.ecs.util

sealed case class Side()

object Side {
  object Left extends Side
  object Right extends Side
  object Top extends Side
  object Bottom extends Side
  object Center extends Side
}
