package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, PositionComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

object AISystem {
  def apply(coordinator: Coordinator): System = new InputSystemImpl(coordinator)
  private class InputSystemImpl(coordinator: Coordinator)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[DirectionComponent])) {

    def update(): Unit = {

    }
  }
}
