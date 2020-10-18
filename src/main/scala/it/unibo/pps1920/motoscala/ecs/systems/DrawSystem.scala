package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.DrawEntityEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.DrawEntityData
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Direction
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

object DrawSystem {
  def apply(mediator: Mediator, coordinator: Coordinator,
            myUuid: UUID): System = new DrawSystemImpl(mediator, coordinator, myUuid)

  private class DrawSystemImpl(mediator: Mediator, coordinator: Coordinator, myUuid: UUID)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[VelocityComponent], classOf[ShapeComponent])) {
    override def update(): Unit = {
      val entitiesToView = entitiesRef().collect(e => {
        val p = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
        val s = coordinator.getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent]
        val v = coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
        DrawEntityData(p.pos, Direction.vecToDir(v.currentVel), s.shape, e)
      }).partition(_.entity.uuid == myUuid)
      mediator.publishEvent(DrawEntityEvent(entitiesToView._1.headOption, entitiesToView._2))
    }
  }
}
