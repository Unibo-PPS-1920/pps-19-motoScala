package it.unibo.pps1920.motoscala.ecs.util

import it.unibo.pps1920.motoscala.ecs.util.Direction._


/**
 * Abstraction for cardinal directions
 */


sealed case class Direction(value: Vector2) {
  /**
   * Sums two directions
   *
   * @param dir direction to be summed
   * @return resulting direction
   */
  def +(dir: Direction): Direction = Direction((dir.value add value).clip())

  /**
   * Inverts a direction
   *
   * @return opposite direction
   */
  def opposite(): Direction = this match {
    case Center => Center
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
  def vecToDir(vec: Vector2): Direction = Direction(vec.clip())
}

