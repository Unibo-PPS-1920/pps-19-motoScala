package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import it.unibo.pps1920.motoscala.engine.CommandQueue

object InputSystem {
  def apply(coordinator: Coordinator,
            eventQueue: CommandQueue): System = new InputSystemImpl(coordinator, eventQueue)
  private class InputSystemImpl(coordinator: Coordinator, eventQueue: CommandQueue)
    extends AbstractSystem(ECSSignature(classOf[VelocityComponent])) {

    def update(): Unit = {
      val events = eventQueue.dequeueAll()
      entitiesRef().foreach(e => {
        val vC = coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
        events.filter(_.cmd.entity == e).map(_.cmd.direction).lastOption match {
          case Some(dir) => vC.vel.x = (dir.value.x * vC.defVel.x); vC.vel.y = (dir.value.y * vC.defVel.y) //optimized
          case None => //leave old direction
        }
      })
    }
  }
}

