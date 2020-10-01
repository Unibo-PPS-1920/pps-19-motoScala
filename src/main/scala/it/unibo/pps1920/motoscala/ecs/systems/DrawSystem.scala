package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.EventData.DrawEntityData
import it.unibo.pps1920.motoscala.controller.mediation.{Event, Mediator}
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, PositionComponent, ShapeComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import org.slf4j.LoggerFactory

object DrawSystem {
  def apply(mediator: Mediator, coordinator: Coordinator,
            myUuid: UUID): System = new DrawSystemImpl(mediator, coordinator, myUuid)

  private class DrawSystemImpl(mediator: Mediator, coordinator: Coordinator, myUuid: UUID)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[DirectionComponent], classOf[ShapeComponent])) {
    private val logger = LoggerFactory getLogger classOf[DrawSystemImpl]
    override def update(): Unit = {
      val entitiesToView = entitiesRef().collect(e => {
        val p = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
        val s = coordinator.getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent]
        val d = coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
        DrawEntityData(p.pos, d.dir, s.shape, e)
      }).partition(d => d.entity.uuid == myUuid)
      mediator.publishEvent(Event.DrawEntityEvent(entitiesToView._1.head, entitiesToView._2.toSeq))
    }
  }
}
