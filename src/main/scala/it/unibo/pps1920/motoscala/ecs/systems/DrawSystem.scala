package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.mediation.Event.DrawEntityEvent
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

object DrawSystem {


  def apply(mediator: Mediator, coordinator: Coordinator): System = new DrawSystemImpl(mediator, coordinator)

  private class DrawSystemImpl(mediator: Mediator, coordinator: Coordinator) extends AbstractSystem {
    override def update(): Unit = {
      val entitiesToView = entitiesRef()
        .collect(e => {
          import it.unibo.pps1920.motoscala.controller.mediation.EventData.EntityData
          val p = coordinator
            .getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          val s = coordinator
            .getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent]
          EntityData(p.pos, s.shape, s.color)
        }).toSeq
      mediator.publishEvent(DrawEntityEvent(entitiesToView))
    }
  }

}

