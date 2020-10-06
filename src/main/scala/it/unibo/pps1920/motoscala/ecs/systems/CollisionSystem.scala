package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Entity, System}


object CollisionSystem {
  def apply(coordinator: Coordinator, fps: Int): System = new CollisionSystemImpl(coordinator, fps)
  private class CollisionSystemImpl(coordinator: Coordinator, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent],
                                        classOf[DirectionComponent],
                                        classOf[ShapeComponent],
                                        classOf[CollisionComponent],
                                        classOf[VelocityComponent])) {

    def update(): Unit = {
      entitiesRef().foreach(e => {
        val col = coordinator.getEntityComponent(e, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
        val dir = coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
        //collision already happening
        if (col.duration > 0) {
          dir.dir = col.colDirection
          col.duration -= 1
          if (col.duration <= 0) {
            logger info s"${col.duration} ${col.inDirection}"
            dir.dir = col.inDirection
            coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent].vel = col
              .oldSpeed
          }
        } else {
          //check for new possible collisions
          val shapeC = coordinator.getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent]
          val posC = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          entitiesRef().filter(other => other.uuid != e.uuid).foreach(other => {
            val shapeCo = coordinator.getEntityComponent(other, classOf[ShapeComponent]).get
              .asInstanceOf[ShapeComponent]
            val posCo = coordinator.getEntityComponent(other, classOf[PositionComponent]).get
              .asInstanceOf[PositionComponent]
            (shapeC.shape, shapeCo.shape) match {
              case (Circle(radius), Circle(radius2)) => if ((posC.pos dist posCo.pos) < (radius + radius2))
                bounce(e, other)
              case _ =>
            }
          })
        }
      })
    }
    def bounce(e1: Entity, e2: Entity): Unit = {
      val dir1 = coordinator.getEntityComponent(e1, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
      val dir2 = coordinator.getEntityComponent(e2, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
      logger info s"bouncing $dir1 off $dir2"
      //calculate new directions
      val col1 = coordinator.getEntityComponent(e1, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
      val col2 = coordinator.getEntityComponent(e2, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
      col1.inDirection = dir1.dir
      col2.inDirection = dir2.dir
      col1.colDirection = dir1.dir.opposite()
      col2.colDirection = dir2.dir.opposite()
      val duration1: Int = (((col1.mass * fps) / (10 * fps)) * (fps / 2)).toInt
      val duration2: Int = (((col2.mass * fps) / (10 * fps)) * (fps / 2)).toInt
      col1.duration = duration1
      col2.duration = duration2
      val vel1 = coordinator.getEntityComponent(e1, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
      val vel2 = coordinator.getEntityComponent(e2, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
      col1.oldSpeed = vel1.vel
      col2.oldSpeed = vel2.vel
      vel1.vel = 30
      vel2.vel = 30
    }
  }
}