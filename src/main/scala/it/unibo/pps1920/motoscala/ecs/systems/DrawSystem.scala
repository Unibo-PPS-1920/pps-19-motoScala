package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.DrawEntityEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.EntityData
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.core.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Direction
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
/**
 * System for generating display data to be drawn on the canvas
 */
object DrawSystem {
  def apply(mediator: Mediator, coordinator: Coordinator,
            UUIDs: List[UUID]): System = new DrawSystemImpl(mediator, coordinator, UUIDs)

  private class DrawSystemImpl(mediator: Mediator, coordinator: Coordinator, UUIDs: List[UUID])
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[VelocityComponent], classOf[ShapeComponent])) {
    override def update(): Unit = {

      val entitiesToView = entitiesRef().collect(e => {
        val p = coordinator.getEntityComponent[PositionComponent](e)
        val s = coordinator.getEntityComponent[ShapeComponent](e)
        val v = coordinator.getEntityComponent[VelocityComponent](e)
        EntityData(p.pos, Direction.vecToDir(v.currentVel), s.shape, e)
      }).partition(x => UUIDs.contains(x.entity.uuid))
      mediator.publishEvent(DrawEntityEvent(entitiesToView._1.map(e => Some(e)), entitiesToView._2))
    }
  }
}
