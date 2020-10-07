package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import it.unibo.pps1920.motoscala.engine.CommandQueue
import it.unibo.pps1920.motoscala.ecs.util.Vector2

object InputSystem {
  def apply(coordinator: Coordinator,
            eventQueue: CommandQueue): System = new InputSystemImpl(coordinator, eventQueue)
  private class InputSystemImpl(coordinator: Coordinator, eventQueue: CommandQueue)
    extends AbstractSystem(ECSSignature(classOf[VelocityComponent], classOf[DirectionComponent])) {

    def update(): Unit = {
      val events = eventQueue.dequeueAll()
      entitiesRef().foreach(e => {
        val vC = coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
        val dirOption = events.filter(_.cmd.entity == e).map(_.cmd.direction).lastOption
        dirOption match {
          case Some(dir) => vC.vel = Vector2(dir.value.x * vC.vel.x, dir.value.y * vC.vel.y)
          case None =>
        }
      })
    }
  }
}

