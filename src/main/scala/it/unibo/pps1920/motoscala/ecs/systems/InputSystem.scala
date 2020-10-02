package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Direction
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
        //sum all active direction events to obtain final direction
        val dir = events.filter(ev => ev.cmd.entity == e && ev.cmd.moving).map(_.cmd.direction)
          .foldLeft[Direction](coordinator.getEntityComponent(e, classOf[DirectionComponent]).get
                                 .asInstanceOf[DirectionComponent]
                                 .dir)(_ + _)

        coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
          .dir = dir
        coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
          .vel = deltaVelocity
      })
    }
  }

}

