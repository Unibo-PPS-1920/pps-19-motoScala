package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.mediation.Event.LevelEndEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.EndData
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.components.PositionComponent
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

object EndGameSystem {
  def apply(coordinator: Coordinator, mediator: Mediator,
            canvasSize: Vector2): System = new EndGameSystemImpl(coordinator, mediator, canvasSize)
  private class EndGameSystemImpl(coordinator: Coordinator, mediator: Mediator, canvasSize: Vector2)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent])) {
    override def update(): Unit = {
      entitiesRef()
        .filter(e => {
          val p = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent].pos
          p.x < 0 || p.y < 0 || p.x > canvasSize.x || p.y > canvasSize.y
        }).foreach(e => {
        if (e.getClass == classOf[BumperCarEntity]) {
          mediator.publishEvent(LevelEndEvent(EndData(e)))
        } else {
          coordinator.removeEntity(e)
        }
      })
    }
  }
}