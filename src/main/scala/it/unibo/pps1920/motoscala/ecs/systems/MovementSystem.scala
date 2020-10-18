package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

object MovementSystem {
  def apply(coordinator: Coordinator, fps: Int): System = new MovementSystemImpl(coordinator, fps)
  private class MovementSystemImpl(coordinator: Coordinator, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[VelocityComponent])) {
    override def update(): Unit = {
      entitiesRef()
        .foreach(e => {
          val p = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          val v = coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
          p.pos = p.pos add v.currentVel.dot(1.0 / fps)
        })
    }
  }
}