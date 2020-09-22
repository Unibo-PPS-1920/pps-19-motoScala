package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.mediation.Event.DrawEntityEvent
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.AbstractSystem
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator

trait DrawSystem extends AbstractSystem {

}

object DrawSystem {


  def apply(mediator: Mediator, coordinator: Coordinator): DrawSystem = new DrawSystemImpl(mediator, coordinator)

  private class DrawSystemImpl(mediator: Mediator, coordinator: Coordinator) extends DrawSystem {
    override def update(): Unit = {
      val entitiesToView = entitiesRef()
        .collect(e => {
          val p = coordinator
            .getEntityComponent(e, PositionComponent.getClass).asInstanceOf[PositionComponent]
          val s = coordinator
            .getEntityComponent(e, ShapeComponent.getClass).asInstanceOf[ShapeComponent]
          ((p.x, p.y, p.z), s.shape, s.color)
        }).toSeq
      mediator.publishEvent(DrawEntityEvent(entitiesToView))
    }
  }

}

