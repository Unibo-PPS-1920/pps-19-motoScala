package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.AbstractSystem


trait InputSystem extends AbstractSystem {

}

object InputSystem{
  import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandableEvent
  import it.unibo.pps1920.motoscala.controller.mediation.Mediator
  import it.unibo.pps1920.motoscala.ecs.managers.Coordinator

  import scala.collection.mutable
  def apply(mediator: Mediator, coordinator: Coordinator, eventQueue: mutable.Queue[CommandableEvent]): InputSystem = new InputSystemImpl(mediator, coordinator, eventQueue)

  private class InputSystemImpl(mediator: Mediator, coordinator: Coordinator, eventQueue: mutable.Queue[CommandableEvent]) extends InputSystem {
    def update(): Unit = {
      import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
      import it.unibo.pps1920.motoscala.ecs.components.DirectionComponent
      eventQueue.dequeue() match {
        case CommandEvent(direction, entity) =>
          if(entitiesRef().contains(entity)) coordinator.addEntityComponent(entity, DirectionComponent(direction))
        case _ => ???
      }

    }
  }
}

