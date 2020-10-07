package it.unibo.pps1920.motoscala.model

import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.util.Vector2

object Level {
  sealed trait LevelEntity
  case class LevelData12(index: Int, mapSize: Vector2, entities: List[LevelEntity])
  case class LevelData(index: Int, mapSize: Coordinate, entities: List[LevelEntity])
  case class Coordinate(x: Int, y: Int)

  case class Player(position: Coordinate, shape: Circle, direction: Coordinate, velocity: Coordinate) extends LevelEntity
  case class RedPupa(position: Coordinate, shape: Circle, direction: Coordinate, velocity: Coordinate) extends LevelEntity
  case class BluePupa(position: Coordinate, shape: Circle, direction: Coordinate, velocity: Coordinate) extends LevelEntity
  case class BlackPupa(position: Coordinate, shape: Circle, direction: Coordinate, velocity: Coordinate) extends LevelEntity
  case class Polar(position: Coordinate, shape: Circle, direction: Coordinate, velocity: Coordinate) extends LevelEntity

}

