package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import it.unibo.pps1920.motoscala.engine.CommandQueue


object InputSystem {

  def apply(coordinator: Coordinator,
            eventQueue: CommandQueue): System = new InputSystemImpl(coordinator, eventQueue)

  private class InputSystemImpl(coordinator: Coordinator,
                                eventQueue: CommandQueue)
    extends AbstractSystem(ECSSignature(classOf[VelocityComponent], classOf[DirectionComponent])) {

    val deltaVelocity: Double = 1
    def update(): Unit = {
      val events = eventQueue.dequeueAll()
      entitiesRef().foreach(e => {
        events.filter(_.cmd.entity == e).foreach({
          case CommandEvent(CommandData(entity, direction, isActive)) =>
            logger info "ciao"
            coordinator.getEntityComponent(entity, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
              .dir = direction
            coordinator.getEntityComponent(entity, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
              .vel = deltaVelocity
          case _ =>
        })
      })
    }
  }

}