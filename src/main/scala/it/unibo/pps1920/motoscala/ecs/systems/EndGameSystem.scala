package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.controller.mediation.Event.LevelEndEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData
import it.unibo.pps1920.motoscala.ecs.components.{CollisionComponent, PositionComponent, ScoreComponent}
import it.unibo.pps1920.motoscala.ecs.core.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Entity, System}
import it.unibo.pps1920.motoscala.engine.Engine

/**
 * System for handling removal of killed entities and to determine the end of a game
 */
object EndGameSystem {
  def apply(coordinator: Coordinator, controller: EngineController,
            canvasSize: Vector2,
            engine: Engine): System = new EndGameSystemImpl(coordinator, controller, canvasSize, engine: Engine)
  private class EndGameSystemImpl(coordinator: Coordinator, controller: EngineController, canvasSize: Vector2,
                                  engine: Engine)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[ScoreComponent], classOf[CollisionComponent])) {
    override def update(): Unit = {
      entitiesRef().filter(e => checkOutside(e) || checkLifePoints(e)).foreach(e => {
        if (e.getClass == classOf[BumperCarEntity]) {
          this.engine.stop()
          controller.redirectSoundEvent(PlaySoundEffect(Clips.GameOver))
        } else {
          controller.redirectSoundEvent(PlaySoundEffect(Clips.Out))
        }
        this.controller.mediator.publishEvent(LevelEndEvent(EventData.EndData(hasWon = false, e, coordinator
          .getEntityComponent[ScoreComponent](e).score)))
        this.coordinator.removeEntity(e)
      })
      if (entitiesRef().nonEmpty && entitiesRef().forall(_.getClass == classOf[BumperCarEntity])) {
        controller.redirectSoundEvent(PlaySoundEffect(Clips.Win))
        this.engine.stop()
        this.controller.mediator.publishEvent(LevelEndEvent(EventData.EndData(hasWon = true, entitiesRef().head, 0)))
      }
    }

    private def checkOutside(entity: Entity): Boolean = {
      val p = coordinator.getEntityComponent[PositionComponent](entity).pos
      p.x <= 0 || p.y <= 0 || p.x >= canvasSize.x || p.y >= canvasSize.y
    }

    private def checkLifePoints(entity: Entity): Boolean = {
      val c = coordinator.getEntityComponent[CollisionComponent](entity)
      c.life <= 0
    }
  }
}
