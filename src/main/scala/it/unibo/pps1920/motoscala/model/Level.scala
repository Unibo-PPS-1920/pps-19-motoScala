package it.unibo.pps1920.motoscala.model

import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle

object Level {
  import it.unibo.pps1920.motoscala.ecs.components.Shape.Rectangle
  sealed trait LevelEntity
  case class LevelData(index: Int, mapSize: Coordinate, var entities: List[LevelEntity])
  case class Coordinate(x: Int, y: Int)

  case class Player(position: Coordinate, shape: Circle,
                    velocity: Coordinate) extends LevelEntity
  case class RedPupa(position: Coordinate, shape: Circle,
                     velocity: Coordinate) extends LevelEntity
  case class BluePupa(position: Coordinate, shape: Circle,
                      velocity: Coordinate) extends LevelEntity
  case class BlackPupa(position: Coordinate, shape: Circle,
                       velocity: Coordinate) extends LevelEntity

  case class Polar(position: Coordinate, shape: Circle, velocity: Coordinate) extends LevelEntity
  case class Nabicon(position: Coordinate, shape: Rectangle, velocity: Coordinate) extends LevelEntity
  case class Beecon(position: Coordinate, shape: Circle,
                    velocity: Coordinate) extends LevelEntity
  case class JumpPowerUp(position: Coordinate, shape: Circle) extends LevelEntity
  case class WeightBoostPowerUp(position: Coordinate, shape: Circle) extends LevelEntity
  case class SpeedBoostPowerUp(position: Coordinate, shape: Circle) extends LevelEntity
}

