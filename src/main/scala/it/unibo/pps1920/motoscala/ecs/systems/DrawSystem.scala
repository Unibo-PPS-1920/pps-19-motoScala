package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.EventData.DrawEntityData
import it.unibo.pps1920.motoscala.controller.mediation.{Event, Mediator}
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

import scala.util.Try

object DrawSystem {
  import it.unibo.pps1920.motoscala.ecs.components.VelocityComponent
  def apply(mediator: Mediator, coordinator: Coordinator,
            myUuid: UUID): System = new DrawSystemImpl(mediator, coordinator, myUuid)

  private class DrawSystemImpl(mediator: Mediator, coordinator: Coordinator, myUuid: UUID)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[VelocityComponent], classOf[ShapeComponent])) {
    override def update(): Unit = {
      val entitiesToView = entitiesRef().collect(e => {
        import it.unibo.pps1920.motoscala.ecs.components.VelocityComponent
        import it.unibo.pps1920.motoscala.ecs.util.Direction
        val p = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
        val s = coordinator.getEntityComponent(e, classOf[ShapeComponent]).get.asInstanceOf[ShapeComponent]
        val v = coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
        DrawEntityData(p.pos, Direction.velToDir(v.vel), s.shape, e)
      }).partition(_.entity.uuid == myUuid)
      Try(Event.DrawEntityEvent(entitiesToView._1.head, entitiesToView._2.toSeq))
        .fold(err => logger info err.getMessage, value => mediator.publishEvent(value))


    }
  }
}
