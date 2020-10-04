package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Direction.East.opposite
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Entity, System}

object CollisionSystem {
  def apply(coordinator: Coordinator): System = new InputSystemImpl(coordinator)
  private class InputSystemImpl(coordinator: Coordinator)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[DirectionComponent], classOf[ShapeComponent], classOf[CollisionComponent])) {

    def update(): Unit = {
      entitiesRef().foreach(e => {
        val shape = coordinator.getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent].shape
        val pos = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent].pos
        shape match {
          case Shape.Circle(radius) => entitiesRef()
            .filter(otherE => (otherE.uuid != e.uuid) && (coordinator
              .getEntityComponent(otherE, classOf[ShapeComponent]).get
              .asInstanceOf[ShapeComponent].shape match {
              case Circle(radius2) => val pos2 = coordinator.getEntityComponent(otherE, classOf[PositionComponent]).get
                .asInstanceOf[PositionComponent].pos
                (pos dist pos2) < (radius + radius2)
            })).foreach(e2 => {
            //            logger info "collision"
            bounce(e2)
            bounce(e)
          }
                        )
          case Shape.Rectangle(_, _) =>
        }
      })

    }
    def bounce(e: Entity): Unit = {
      val dir = coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
      dir.dir = opposite(dir.dir)
    }
  }
}