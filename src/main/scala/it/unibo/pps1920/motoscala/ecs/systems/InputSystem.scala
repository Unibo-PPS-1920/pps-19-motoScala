package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandEvent, CommandableEvent}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

import scala.collection.mutable


object InputSystem {

  def apply(coordinator: Coordinator,
            eventQueue: mutable.Queue[CommandableEvent]): System = new InputSystemImpl(coordinator, eventQueue)

  private class InputSystemImpl(coordinator: Coordinator,
                                eventQueue: mutable.Queue[CommandableEvent]) extends AbstractSystem {

    val deltaVelocity: Double = 1
    def update(): Unit = {
      eventQueue.dequeueAll(_ => true).foreach {
        case CommandEvent(CommandData(entity, direction)) =>
          coordinator.getEntityComponent(entity, classOf[DirectionComponent]).asInstanceOf[DirectionComponent]
            .dir = direction
          coordinator.getEntityComponent(entity, classOf[VelocityComponent]).asInstanceOf[VelocityComponent]
            .vel = deltaVelocity
        case _ =>
      }
    }
  }

}

