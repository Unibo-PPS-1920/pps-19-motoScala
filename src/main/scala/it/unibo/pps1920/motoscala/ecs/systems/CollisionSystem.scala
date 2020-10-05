package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Direction._
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Entity, System}


object CollisionSystem {
  def apply(coordinator: Coordinator, fps: Int): System = new CollisionSystemImpl(coordinator, fps)
  private class CollisionSystemImpl(coordinator: Coordinator, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent],
                                        classOf[DirectionComponent],
                                        classOf[ShapeComponent],
                                        classOf[CollisionComponent],
                                        classOf[VelocityComponent])) {

    val deltaCollision: Int = fps / 2
    def update(): Unit = {
      entitiesRef().foreach(e => {
        val col = coordinator.getEntityComponent(e, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
        //collision already happening
        if (col.duration > 0) {
          col.duration -= deltaCollision
          //after collision ends restore to original values
          if (col.duration <= 0) {
            col.direction = Center
            coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent].vel = col
              .speed
          }
          //until collision hasn't ended ignore input
          coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent].dir = col
            .direction
        } else {
          //check for new possible collisions
          val shape = coordinator.getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent].shape
          val pos = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
            .pos
          shape match {
            case Shape.Circle(radius) => entitiesRef()
              //don't collide with myself
              .filter(otherE => (otherE.uuid != e.uuid) && (coordinator
                .getEntityComponent(otherE, classOf[ShapeComponent]).get
                .asInstanceOf[ShapeComponent].shape match {
                case Circle(radius2) => val pos2 = coordinator.getEntityComponent(otherE, classOf[PositionComponent])
                  .get
                  .asInstanceOf[PositionComponent].pos
                  //detect collision
                  (pos dist pos2) < (radius + radius2)
                case Shape.Rectangle(_, _) => false
              })).foreach(e2 => {
              //handle collision
              bounce(e, e2)
            }
                          )
            case Shape.Rectangle(_, _) =>
          }
        }
      }
                            )

    }
    def bounce(e1: Entity, e2: Entity): Unit = {
      val dir1 = coordinator.getEntityComponent(e1, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
      val dir2 = coordinator.getEntityComponent(e2, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
      logger info s"bouncing $dir1 off $dir2"
      //calculate new directions
      dir1.dir = dir1.dir.opposite()
      dir2.dir = dir1.dir.opposite()
      val col1 = coordinator.getEntityComponent(e1, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
      val col2 = coordinator.getEntityComponent(e2, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
      col1.duration = fps
      col2.duration = fps
      col1.direction = dir1.dir
      col2.direction = dir2.dir
      val oldVel1 = coordinator.getEntityComponent(e1, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
      val oldVel2 = coordinator.getEntityComponent(e2, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
      col1.speed = oldVel1.vel
      col2.speed = oldVel2.vel
      oldVel1.vel *= col2.mass
      oldVel2.vel *= col1.mass

    }
  }
}