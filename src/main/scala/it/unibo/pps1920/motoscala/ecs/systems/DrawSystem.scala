package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.mediation.Event.DrawEntityEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.DrawEntityData
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, PositionComponent, ShapeComponent, TypeComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

object DrawSystem {
  def apply(mediator: Mediator, coordinator: Coordinator): System = new DrawSystemImpl(mediator, coordinator)

  private class DrawSystemImpl(mediator: Mediator, coordinator: Coordinator)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[DirectionComponent], classOf[ShapeComponent], classOf[TypeComponent])) {
    override def update(): Unit = {
      val entitiesToView = entitiesRef()
        .collect(e => {
          val p = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          val s = coordinator.getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent]
          val t = coordinator.getEntityComponent(e, classOf[TypeComponent]).get.asInstanceOf[TypeComponent]
          val d = coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
          DrawEntityData(p.pos, d.dir, s.shape, t.enType)
        }).toSeq
      mediator.publishEvent(DrawEntityEvent(entitiesToView))
    }
  }
}
