package it.unibo.pps1920.motoscala.model

import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}

object Level {
  sealed trait LevelEntity
  case class LevelData(index: Int, mapSize: Coordinate, entities: List[LevelEntity])
  case class Coordinate(x: Int, y: Int)
  case class Tile(shape: Rectangle, position: Coordinate, tangible: Boolean) extends LevelEntity
  case class Player(position: Coordinate, shape: Circle, direction: Coordinate, velocity: Double) extends LevelEntity
  case class Enemy1(position: Coordinate, shape: Rectangle, direction: Coordinate, velocity: Double) extends LevelEntity
}

/*package it.unibo.pps1920.motoscala.model

object Level {
  case class LevelData(index: Int, mapSize: (Int, Int), entities: List[LevelEntity])
  case class LevelEntity(entityType: String, shape: ())
}
*/
