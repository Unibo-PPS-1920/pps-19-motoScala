package it.unibo.pps1920.motoscala.ecs.util


/**
 * Abstraction for cardinal directions
 */


sealed case class Direction(value: Vector2) {
  def +(dir: Direction): Direction = {
    Direction((dir.value add value).unit())
  }
}
object Direction {
  object Center extends Direction(Vector2(0, 0))
  object North extends Direction(Vector2(0, -1))
  object NorthWest extends Direction(Vector2(-1, -1))
  object NorthEast extends Direction(Vector2(1, -1))
  object South extends Direction(Vector2(0, 1))
  object SouthWest extends Direction(Vector2(-1, 1))
  object SouthEast extends Direction(Vector2(1, 1))
  object West extends Direction(Vector2(-1, 0))
  object East extends Direction(Vector2(1, 0))
}

