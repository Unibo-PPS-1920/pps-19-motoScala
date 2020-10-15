package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.PowerUpEffect.{JumpPowerUp, SpeedBoostPowerUp}
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity, PowerUpEntity}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class PowerUpSystemTest extends AnyWordSpec with BeforeAndAfterAll with Matchers {

  var coordinator: Coordinator = _
  var powerup: System = _
  var mediator: Mediator = _
  val pid: UUID = UUID.randomUUID()
  val entity = BumperCarEntity(pid)
  val ep1 = PowerUpEntity(UUID.randomUUID())
  val ep2 = PowerUpEntity(UUID.randomUUID())
  val ep3 = PowerUpEntity(UUID.randomUUID())
  val pos: PositionComponent = PositionComponent((0, 0))
  val vel: VelocityComponent = VelocityComponent((0, 20), (20, 20))
  val col: CollisionComponent = CollisionComponent(mass = 2, oldSpeed = (1.0, 2.0))
  var jmp: JumpComponent = JumpComponent()
  val pUp1: PowerUpComponent = PowerUpComponent(effect = SpeedBoostPowerUp(2, _.dot(2)))
  val pUp2: PowerUpComponent = PowerUpComponent(effect = JumpPowerUp(2))
  val pUp3: PowerUpComponent = PowerUpComponent(effect = PowerUpEffect.WeightBoostPowerUp(2, _ * 2))


  val pos2: PositionComponent = PositionComponent((10, 10))

  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    powerup = PowerUpSystem(coordinator)
    coordinator
      .registerComponentType(classOf[PositionComponent])
      .registerComponentType(classOf[VelocityComponent])
      .registerComponentType(classOf[CollisionComponent])
      .registerComponentType(classOf[JumpComponent])
      .registerComponentType(classOf[PowerUpComponent])

      .registerSystem(powerup)

      .addEntity(entity)
      .addEntityComponent(entity, pos)
      .addEntityComponent(entity, vel)
      .addEntityComponent(entity, col)
      .addEntityComponent(entity, jmp)

      .addEntity(ep1)
      .addEntityComponent(ep1, pUp1)

      .addEntity(ep2).addEntityComponent(ep2, pUp2)

      .addEntity(ep3).addEntityComponent(ep3, pUp3)

  }
  "A powerupSystem" when {
    "updating" should {
      "make the player go faster" in {
        vel.inputVel = (1, 1)
        pUp1.entity = Some(entity)
        coordinator.updateSystems()
        vel.inputVel shouldBe Vector2(2, 2)
      }
      "make the player jump for the right amount of ticks" in {
        jmp.isActive shouldBe false
        pUp2.entity = Some(entity)
        coordinator.updateSystems()
        jmp.isActive shouldBe true
        coordinator.updateSystems()
        jmp.isActive shouldBe false
      }
      "make the player gain weight for the right amount of ticks" in {
        col.mass shouldBe 2
        pUp3.entity = Some(entity)
        coordinator.updateSystems()
        col.mass shouldBe 4
        coordinator.updateSystems()
        col.mass shouldBe 2
      }
    }
  }
}
