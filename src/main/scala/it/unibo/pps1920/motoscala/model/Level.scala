package it.unibo.pps1920.motoscala.model

object Level {
  case class LevelData(index: Int, mapSize: (Int, Int), entities: List[LevelEntity])
  case class LevelEntity()
}

