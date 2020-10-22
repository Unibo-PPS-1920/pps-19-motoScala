package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.core.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import it.unibo.pps1920.motoscala.engine.Constants.MaxFps

/**
 * System handling the movement of entities based on their velocity
 */
object MovementSystem {
  /** Factory for [[MovementSystem]] instances */
  def apply(coordinator: Coordinator, fps: Int): System = new MovementSystemImpl(coordinator, fps)

  private class MovementSystemImpl(coordinator: Coordinator, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[VelocityComponent])) {

    override def update(): Unit = {
      entitiesRef()
        .foreach(e => {
          val p = coordinator.getEntityComponent[PositionComponent](e)
          val v = coordinator.getEntityComponent[VelocityComponent](e)
          p.pos = p.pos add v.currentVel.dot(MaxFps / fps)
        })
    }
  }
}