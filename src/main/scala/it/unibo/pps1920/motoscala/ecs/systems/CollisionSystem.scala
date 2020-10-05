package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Direction._
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Entity, System}


object CollisionSystem {
  def apply(coordinator: Coordinator, fps: Int): System = new CollisionSystemImpl(coordinator, fps)
  private class CollisionSystemImpl(coordinator: Coordinator, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[DirectionComponent], classOf[ShapeComponent], classOf[CollisionComponent])) {

    def update(): Unit = {
      entitiesRef().foreach(e => {
        val col = coordinator.getEntityComponent(e, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
        val collisionDuration: Int = fps * col.mass
        val deltaCollision: Int = (fps / 3).ceil.toInt
        if (col
          .duration > 0) {
          col.duration -= deltaCollision
          if (col.duration <= 0) col.direction = Center
          coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent].dir = col
            .direction
        } else {
          val shape = coordinator.getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent].shape
          val pos = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
            .pos
          shape match {
            case Shape.Circle(radius) => entitiesRef()
              .filter(otherE => (otherE.uuid != e.uuid) && (coordinator
                .getEntityComponent(otherE, classOf[ShapeComponent]).get
                .asInstanceOf[ShapeComponent].shape match {
                case Circle(radius2) => val pos2 = coordinator.getEntityComponent(otherE, classOf[PositionComponent])
                  .get
                  .asInstanceOf[PositionComponent].pos
                  (pos dist pos2) < (radius + radius2)
                case Shape.Rectangle(_, _) => false
              })).foreach(e2 => {
              bounce(e, e2, collisionDuration)
            }
                          )
            case Shape.Rectangle(_, _) =>
          }
        }
      }
                            )

    }
    def bounce(e1: Entity, e2: Entity, collisionDuration: Int): Unit = {
      val dir1 = coordinator.getEntityComponent(e1, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
      val dir2 = coordinator.getEntityComponent(e2, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
      logger info s"bouncing $dir1 off $dir2"
      dir1.dir = dir1.dir.opposite()
      dir2.dir = dir1.dir.opposite()
      val col1 = coordinator.getEntityComponent(e1, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
      val col2 = coordinator.getEntityComponent(e2, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
      col1.duration = collisionDuration
      col2.duration = collisionDuration
      col1.direction = dir1.dir
      col2.direction = dir2.dir
    }
  }
}