package it.unibo.pps1920.motoscala.model

import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}

object Level {
  sealed trait Entity
  case class LevelData(index: Int, mapSize: (Int, Int), entities: List[Entity])
  case class LevelData2(index: Int, mapSize: Coordinate,
                        entities: List[Entity])
  case class Coordinate(x: Int, y: Int)

  case class TileEntity(shape: Rectangle, position: Coordinate,
                        tangible: Boolean) extends Entity

  case class PlayerEntity(position: Coordinate, shape: Circle, tangible: Boolean, direction: Coordinate,
                          velocity: Int) extends Entity

  case class EnemyEntity(position: Coordinate, shape: Rectangle, tangible: Boolean,
                         direction: Coordinate,
                         velocity: Int) extends Entity


}

/*package it.unibo.pps1920.motoscala.model

object Level {
  case class LevelData(index: Int, mapSize: (Int, Int), entities: List[LevelEntity])
  case class LevelEntity(entityType: String, shape: ())
}
*/
