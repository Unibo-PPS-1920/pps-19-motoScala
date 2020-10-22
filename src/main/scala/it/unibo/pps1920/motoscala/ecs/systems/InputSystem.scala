package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.VelocityComponent
import it.unibo.pps1920.motoscala.ecs.core.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import it.unibo.pps1920.motoscala.engine.CommandQueue

/** An Input System gets inputs from the users and use them to give direction to entities. */
object InputSystem {
  /** Factory for [[InputSystem]] instances */
  def apply(coordinator: Coordinator, eventQueue: CommandQueue): System = new InputSystemImpl(coordinator, eventQueue)

  private class InputSystemImpl(coordinator: Coordinator, eventQueue: CommandQueue)
    extends AbstractSystem(ECSSignature(classOf[VelocityComponent])) {

    override def update(): Unit = {
      val events = eventQueue.dequeueAll()
      entitiesRef().foreach(e => {
        val vC = coordinator.getEntityComponent[VelocityComponent](e)
        events.filter(_.cmd.entity == e).map(_.cmd.direction).lastOption match {
          case Some(dir) => vC.inputVel = dir.value mul vC.defVel
          case None => //leave old direction
        }
      })
    }
  }
}

