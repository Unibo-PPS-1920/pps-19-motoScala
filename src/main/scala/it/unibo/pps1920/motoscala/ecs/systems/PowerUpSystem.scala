package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.PowerUpEffect.WeightBoostPowerUp
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

import scala.language.postfixOps

object PowerUpSystem {
  def apply(coordinator: Coordinator): System = new PowerUpSystemImpl(coordinator)
  private class PowerUpSystemImpl(coordinator: Coordinator)
    extends AbstractSystem(ECSSignature(classOf[PowerUpComponent])) {

    def update(): Unit = {
      entitiesRef()
        .foreach(e => {
          val pUp = coordinator.getEntityComponent(e, classOf[PowerUpComponent]).get.asInstanceOf[PowerUpComponent]
          pUp match {
            case PowerUpComponent(Some(entity), effect) => effect match {
              case PowerUpEffect.SpeedBoostPowerUp(_, modifier) => val affComp = coordinator
                .getEntityComponent(entity, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
                affComp.inputVel = modifier(affComp.inputVel)
                effect.duration = effect.duration - 1
                if (effect.duration == 0) {
                  pUp.entity = None
                }
              case PowerUpEffect.JumpPowerUp(_) => val affComp = coordinator
                .getEntityComponent(entity, classOf[JumpComponent]).get.asInstanceOf[JumpComponent]
                affComp.isActive = true
                effect.duration = effect.duration - 1
                if (effect.duration == 0) {
                  pUp.entity = None
                  affComp.isActive = false
                }

              case PowerUpEffect.WeightBoostPowerUp(_, modifier, oldMass) => val affComp = coordinator
                .getEntityComponent(entity, classOf[CollisionComponent]).get.asInstanceOf[CollisionComponent]
                //ugly workaround to modify oldmass
                effect.asInstanceOf[WeightBoostPowerUp].oldMass = affComp.mass
                affComp.mass = modifier(affComp.mass)
                effect.duration = effect.duration - 1
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
