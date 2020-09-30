package it.unibo.pps1920.motoscala.model

import it.unibo.pps1920.motoscala.controller.mediation.EntityType
import it.unibo.pps1920.motoscala.ecs.util.Vector2

object Level {
  case class LevelData(index: Int, mapSize: (Int, Int), entities: List[LevelEntity])
  case class LevelEntity(enType: EntityType, position: Vector2)
}

