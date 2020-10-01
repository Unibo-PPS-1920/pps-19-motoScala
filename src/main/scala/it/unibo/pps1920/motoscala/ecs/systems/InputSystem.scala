package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandEvent, CommandableEvent}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

import scala.collection.mutable


object InputSystem {

  def apply(coordinator: Coordinator,
            eventQueue: mutable.Queue[CommandableEvent]): System = new InputSystemImpl(coordinator, eventQueue)

  private class InputSystemImpl(coordinator: Coordinator,
                                eventQueue: mutable.Queue[CommandableEvent])
    extends AbstractSystem(ECSSignature(classOf[VelocityComponent], classOf[DirectionComponent])) {

    val deltaVelocity: Double = 1
    def update(): Unit = {
      eventQueue.dequeueAll(_ => true).foreach {
        case CommandEvent(CommandData(entity, direction, isActive)) =>
          coordinator.getEntityComponent(entity, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
            .dir = direction
          coordinator.getEntityComponent(entity, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
            .vel = deltaVelocity
        case _ =>
      }
    }
  }

}