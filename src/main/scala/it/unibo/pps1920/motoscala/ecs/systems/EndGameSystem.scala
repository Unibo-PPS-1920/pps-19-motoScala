package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.controller.mediation.Event.{LevelEndEvent, RedirectSoundEvent}
import it.unibo.pps1920.motoscala.controller.mediation.{EventData, Mediator}
import it.unibo.pps1920.motoscala.ecs.components.PositionComponent
import it.unibo.pps1920.motoscala.ecs.core.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import it.unibo.pps1920.motoscala.engine.Engine

object EndGameSystem {
  def apply(coordinator: Coordinator, mediator: Mediator,
            canvasSize: Vector2,
            engine: Engine): System = new EndGameSystemImpl(coordinator, mediator, canvasSize, engine: Engine)
  private class EndGameSystemImpl(coordinator: Coordinator, mediator: Mediator, canvasSize: Vector2, engine: Engine)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent])) {
    override def update(): Unit = {
      entitiesRef()
        .filter(e => {
          val p = coordinator.getEntityComponent[PositionComponent](e).pos
          p.x < 0 || p.y < 0 || p.x > canvasSize.x || p.y > canvasSize.y
        }).foreach(e => {
        if (e.getClass == classOf[BumperCarEntity]) {
          this.engine.stop()
          this.coordinator.removeEntity(e)
          mediator.publishEvent(RedirectSoundEvent(PlaySoundEffect(Clips.GameOver)))
          this.mediator.publishEvent(LevelEndEvent(EventData.EndData(hasWon = false, e)))
        } else {
          mediator.publishEvent(RedirectSoundEvent(PlaySoundEffect(Clips.Out)))
          this.coordinator.removeEntity(e)
        }
      })
      if (entitiesRef().size == 1 && entitiesRef().head.getClass == classOf[BumperCarEntity]) {
        mediator.publishEvent(RedirectSoundEvent(PlaySoundEffect(Clips.Win)))
        this.engine.stop()
        this.mediator.publishEvent(LevelEndEvent(EventData.EndData(hasWon = true, entitiesRef().head)))
      }
    }
  }
}
