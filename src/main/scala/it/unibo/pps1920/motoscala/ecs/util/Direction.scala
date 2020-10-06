package it.unibo.pps1920.motoscala.ecs.util

import it.unibo.pps1920.motoscala.ecs.util.Direction._


/**
 * Abstraction for cardinal directions
 */


sealed case class Direction(value: Vector2) {
  def +(dir: Direction): Direction = Direction((dir.value add value).unit())


  def angle(): Int = this match {
    case Direction.Center => 0
    case Direction.North => 0
    case Direction.NorthWest => 315
    case Direction.NorthEast => 45
    case Direction.South => 180
    case Direction.SouthWest => 225
    case Direction.SouthEast => 135
    case Direction.West => 270
    case Direction.East => 90
    case _ => -1
  }

  def opposite(): Direction = this match {
    case Center => Center //Direction(Vector2(-1 * math.random(), -1 * math.random()).unit())
    case North => South
    case NorthWest => SouthEast
    case NorthEast => SouthWest
    case South => North
    case SouthWest => NorthEast
    case SouthEast => NorthWest
    case West => East
    case East => West
    case _ => Center
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

