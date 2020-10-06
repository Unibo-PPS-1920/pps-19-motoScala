package it.unibo.pps1920.motoscala.model

import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}
import it.unibo.pps1920.motoscala.ecs.util.Vector2

object Level {
  sealed trait LevelEntity
  case class LevelData12(index: Int, mapSize: Vector2, entities: List[LevelEntity])
  case class LevelData(index: Int, mapSize: Coordinate, floorSize: Coordinate, entities: List[LevelEntity])
  case class Coordinate(x: Int, y: Int)
  case class Tile(shape: Rectangle, position: Coordinate, tangible: Boolean) extends LevelEntity
  case class Player(position: Coordinate, shape: Circle, direction: Coordinate, velocity: Double) extends LevelEntity
  case class Enemy1(position: Coordinate, shape: Circle, direction: Coordinate, velocity: Double) extends LevelEntity
}

