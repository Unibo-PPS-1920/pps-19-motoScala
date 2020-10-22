package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.core.Coordinator
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MovementSystemTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {
  var coordinator: Coordinator = _
  var movement: System = _
  var mediator: Mediator = _
  val entity = TestEntity(UUID.randomUUID())
  val entity2 = TestEntity(UUID.randomUUID())
  val pos: PositionComponent = PositionComponent(Vector2(0, 0))
  val vel: VelocityComponent = VelocityComponent(Vector2(0, 20), Vector2(20, 20))
  val pos2: PositionComponent = PositionComponent(Vector2(10, 10))
  val vel2: VelocityComponent = VelocityComponent(Vector2(20, 0), Vector2(20, 20))
  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    movement = MovementSystem(coordinator, fps = 60)
    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[VelocityComponent])
    coordinator.registerSystem(movement)
    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, pos)
    coordinator.addEntityComponent(entity, vel)
  }

  "A movementSystem" when {
    "updating" should {
      "move an entity to direction" in {
        coordinator.getEntityComponent[PositionComponent](entity).pos shouldBe Vector2(0, 0)
        coordinator.updateSystems()
        coordinator.getEntityComponent[PositionComponent](entity).pos shouldBe Vector2(0, 20)
        coordinator.getEntityComponent[VelocityComponent](entity).currentVel shouldBe Vector2(0, 20)
      }
      "move multiple entities to direction" in {
        coordinator.addEntity(entity2)
        coordinator.addEntityComponent(entity2, pos2)
        coordinator.addEntityComponent(entity2, vel2)
        coordinator.getEntityComponent[VelocityComponent](entity).currentVel = Vector2(20, 0)
        coordinator.updateSystems()
        coordinator.getEntityComponent[PositionComponent](entity2).pos shouldBe Vector2(30, 10)
        coordinator.getEntityComponent[PositionComponent](entity).pos shouldBe Vector2(20, 20)
      }
    }
  }
}
