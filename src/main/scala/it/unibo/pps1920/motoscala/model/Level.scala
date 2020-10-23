package it.unibo.pps1920.motoscala.model

import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}

/** Contains all possible entities in one level, whit their characteristic,
 * contains even the level map size and level index.
 * <p><p><p><p>
 * All entity contain basic information needed for construct ecs respectively entity.
 * The entity are serialized and deserialized, for reconstruct one specific level, the size are used for the rendering
 * the map and the index is used for order level.
 *
 *
 */
object Level {
  /** Represents one entity inside the game. */
  sealed trait LevelEntity
  /** Represents one level, whiteout ecs elements abstraction.
   * The level index, map coordinates and all entities.
   *
   * @param index The level index
   * @param mapSize The map size
   * @param entities The list of level entities
   */
  case class LevelData(index: Int, mapSize: Coordinate, var entities: List[LevelEntity])
  /** Represents a cartesian 2D coordinate.
   *
   * @param x Coordinate
   * @param y Coordinate
   */
  case class Coordinate(x: Int, y: Int)

  case class Player(position: Coordinate, shape: Circle, velocity: Coordinate) extends LevelEntity
  case class RedPupa(position: Coordinate, shape: Circle, velocity: Coordinate) extends LevelEntity
  case class BluePupa(position: Coordinate, shape: Circle, velocity: Coordinate) extends LevelEntity
  case class BlackPupa(position: Coordinate, shape: Circle, velocity: Coordinate) extends LevelEntity
  case class Polar(position: Coordinate, shape: Circle, velocity: Coordinate) extends LevelEntity
  case class BigBoss(position: Coordinate, shape: Circle, velocity: Coordinate) extends LevelEntity
  case class Nabicon(position: Coordinate, shape: Rectangle, velocity: Coordinate) extends LevelEntity
  case class Beecon(position: Coordinate, shape: Rectangle, velocity: Coordinate) extends LevelEntity
  case class JumpPowerUp(position: Coordinate, shape: Circle) extends LevelEntity
  case class WeightBoostPowerUp(position: Coordinate, shape: Circle) extends LevelEntity
  case class SpeedBoostPowerUp(position: Coordinate, shape: Circle) extends LevelEntity
}

