package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import it.unibo.pps1920.motoscala.engine.CommandQueue

object InputSystem {
  def apply(coordinator: Coordinator,
            eventQueue: CommandQueue): System = new InputSystemImpl(coordinator, eventQueue)
  private class InputSystemImpl(coordinator: Coordinator, eventQueue: CommandQueue)
    extends AbstractSystem(ECSSignature(classOf[VelocityComponent], classOf[DirectionComponent])) {

    def update(): Unit = {
      val events = eventQueue.dequeueAll()
      entitiesRef().foreach(e => {
        val dirC = coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
        dirC.dir = events.filter(_.cmd.entity == e).map(_.cmd.direction).lastOption.fold(dirC.dir)(e => e)
      })
    }
  }
}

