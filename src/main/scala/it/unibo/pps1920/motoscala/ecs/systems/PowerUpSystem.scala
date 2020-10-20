package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.managers.audio.{Clips, MediaEvent}
import it.unibo.pps1920.motoscala.controller.mediation.Event.RedirectSoundEvent
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.components.PowerUpEffect.WeightBoostPowerUp
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.core.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

import scala.language.postfixOps

/**
 * System handling the application of powerup effects to the targeted entities
 */

object PowerUpSystem {
  def apply(coordinator: Coordinator, mediator: Mediator): System = new PowerUpSystemImpl(coordinator, mediator)
  private class PowerUpSystemImpl(coordinator: Coordinator, mediator: Mediator)
    extends AbstractSystem(ECSSignature(classOf[PowerUpComponent])) {

    def update(): Unit = {
      entitiesRef()
        .foreach(e => {
          val pUp = coordinator.getEntityComponent[PowerUpComponent](e)
          pUp match {
            case PowerUpComponent(Some(entity), effect) => effect match {
              case PowerUpEffect.SpeedBoostPowerUp(_, modifier) => val affComp = coordinator
                .getEntityComponent[VelocityComponent](entity)
                affComp.inputVel = modifier(affComp.inputVel)
                effect.duration = effect.duration - 1
                mediator.publishEvent(RedirectSoundEvent(MediaEvent.PlaySoundEffect(Clips.PowerUp3)))
                if (effect.duration == 0) {
                  pUp.entity = None
                }
              case PowerUpEffect.JumpPowerUp(_) => val affComp = coordinator
                .getEntityComponent[JumpComponent](entity)
                affComp.isActive = true
                effect.duration = effect.duration - 1
                mediator.publishEvent(RedirectSoundEvent(MediaEvent.PlaySoundEffect(Clips.PowerUp2)))
                if (effect.duration == 0) {
                  pUp.entity = None
                  affComp.isActive = false
                }

              case PowerUpEffect.WeightBoostPowerUp(_, modifier, oldMass) => val affComp = coordinator
                .getEntityComponent[CollisionComponent](entity)
                effect.asInstanceOf[WeightBoostPowerUp].oldMass = affComp.mass
                affComp.mass = modifier(affComp.mass)
                effect.duration = effect.duration - 1
                mediator.publishEvent(RedirectSoundEvent(MediaEvent.PlaySoundEffect(Clips.PowerUp1)))
                if (effect.duration == 0) {
                  affComp.mass = oldMass
                  pUp.entity = None
                }
              case _ =>
            }
            case PowerUpComponent(None, _) =>
          }
        })
    }

  }

}
