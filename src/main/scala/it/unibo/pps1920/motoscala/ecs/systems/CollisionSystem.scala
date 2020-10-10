package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Direction
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Entity, System}


object CollisionSystem {
  def apply(coordinator: Coordinator, fps: Int): System = new CollisionSystemImpl(coordinator, fps)
  private class CollisionSystemImpl(coordinator: Coordinator, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent],
                                        classOf[DirectionComponent],
                                        classOf[ShapeComponent],
                                        classOf[VelocityComponent])) {

    def update(): Unit = {
      entitiesRef().foreach(e => {
        val col = coordinator.getEntityComponent(e, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
        val dir = coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
        //collision already happening
        if (col.isColliding) {
          if (col.duration > 0) {
            col.duration -= 1
            dir.dir = col.colDirection
          } else {
            dir.dir = Direction.Center
            //coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent].vel = col
            //  .oldSpeed
            col.isColliding = false
          }
        } else {
          val shapeC = coordinator.getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent]
          val posC = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          entitiesRef().filter(other => other.uuid != e.uuid).foreach(other => {
            val shapeCo = coordinator.getEntityComponent(other, classOf[ShapeComponent]).get
              .asInstanceOf[ShapeComponent]
            val posCo = coordinator.getEntityComponent(other, classOf[PositionComponent]).get
              .asInstanceOf[PositionComponent]
            (shapeC.shape, shapeCo.shape) match {
              case (Circle(radius), Circle(radius2)) => if ((posC.pos dist posCo.pos) <= (radius + radius2))
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
      val col1 = coordinator.getEntityComponent(e1, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
      val col2 = coordinator.getEntityComponent(e2, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
      col1.inputDirection = dir1.dir
      col1.colDirection = dir2.dir + dir1.dir.opposite()
      val duration1: Int = (((col2.mass * fps) / (10 * fps)) * (fps / 5)).toInt
      col1.duration = duration1
      val vel1 = coordinator.getEntityComponent(e1, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
      //col1.oldSpeed = vel1.vel
      //vel1.vel = 30
      col1.isColliding = true
    }
  }
}