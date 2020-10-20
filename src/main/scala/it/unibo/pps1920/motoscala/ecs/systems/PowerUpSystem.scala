package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.managers.audio.{Clips, MediaEvent}
import it.unibo.pps1920.motoscala.controller.mediation.Event.RedirectSoundEvent
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.components.PowerUpEffect.{JumpPowerUp, SpeedBoostPowerUp, WeightBoostPowerUp}
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.core.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

import scala.language.postfixOps

/**
 * System handling the application of powerup effects to the targeted entities
 */

object PowerUpSystem {
  def apply(coordinator: Coordinator, mediator: Mediator,
            fps: Int): System = new PowerUpSystemImpl(coordinator, mediator, fps)
  private class PowerUpSystemImpl(coordinator: Coordinator, mediator: Mediator, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PowerUpComponent])) {
    def update(): Unit = {
      entitiesRef()
        .foreach(e => {
          val pUp = coordinator.getEntityComponent[PowerUpComponent](e)
          pUp match {
            case PowerUpComponent(Some(entity), effect) => effect match {
              case PowerUpEffect.SpeedBoostPowerUp(_, isActive, modifier, oldVel) =>
                val affComp = coordinator.getEntityComponent[VelocityComponent](entity)
                effect.duration = effect.duration - 1
                if (!isActive) {
                  effect.asInstanceOf[SpeedBoostPowerUp].oldVel = affComp.defVel
                  effect.asInstanceOf[SpeedBoostPowerUp].isActive = true
                  affComp.defVel = modifier(affComp.defVel)
                } else {
                  if (effect.duration % (fps / 2) == 0)
                    mediator.publishEvent(RedirectSoundEvent(MediaEvent.PlaySoundEffect(Clips.PowerUpSpeed)))
                  if (effect.duration == 0) {
                    affComp.defVel = oldVel
                    pUp.entity = None
                  }
                }
              case PowerUpEffect.WeightBoostPowerUp(_, isActive, modifier, oldMass) =>
                val affComp = coordinator.getEntityComponent[CollisionComponent](entity)
                effect.duration = effect.duration - 1
                if (!isActive) {
                  effect.asInstanceOf[WeightBoostPowerUp].oldMass = affComp.mass
                  effect.asInstanceOf[WeightBoostPowerUp].isActive = true
                  affComp.mass = modifier(affComp.mass)
                } else {
                  if (effect.duration % (fps / 2) == 0)
                    mediator.publishEvent(RedirectSoundEvent(MediaEvent.PlaySoundEffect(Clips.PowerUpMass)))
                  if (effect.duration == 0) {
                    affComp.mass = oldMass
                    pUp.entity = None
                  }
                }
              case PowerUpEffect.JumpPowerUp(_, isActive) =>
                val affComp = coordinator.getEntityComponent[JumpComponent](entity)
                effect.duration = effect.duration - 1
                if (!isActive) {
                  effect.asInstanceOf[JumpPowerUp].isActive = true
                  mediator.publishEvent(RedirectSoundEvent(MediaEvent.PlaySoundEffect(Clips.Invisible)))
                }
                affComp.isActive = true
                if (effect.duration % (fps / 2) == 0)
                  mediator.publishEvent(RedirectSoundEvent(MediaEvent.PlaySoundEffect(Clips.PowerUpInvisible)))
                if (effect.duration == 0) {
                  pUp.entity = None
                  affComp.isActive = false
                }
              case _ =>
            }
            case PowerUpComponent(None, _) =>
          }
        })
    }

  }

}
