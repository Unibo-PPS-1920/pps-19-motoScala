package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, PositionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Direction._
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
  val dir: DirectionComponent = DirectionComponent(South)
  val vel: VelocityComponent = VelocityComponent(1)
  val pos2: PositionComponent = PositionComponent(Vector2(0, 0))
  val dir2: DirectionComponent = DirectionComponent(North)
  val vel2: VelocityComponent = VelocityComponent(2)
  override def beforeAll(): Unit = {
    coordinator = Coordinator()

    movement = MovementSystem(coordinator)

    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[VelocityComponent])
    coordinator.registerComponentType(classOf[DirectionComponent])
    coordinator.registerSystem(movement)
    coordinator
      .signSystem(movement, ECSSignature.apply()
        .signComponent(classOf[PositionComponent], classOf[VelocityComponent], classOf[DirectionComponent]))

    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, pos)
    coordinator.addEntityComponent(entity, dir)
    coordinator.addEntityComponent(entity, vel)

  }

  "A movementSystem" when {
    "updating" should {
      "move an entity to direction" in {
        coordinator.getEntityComponent(entity, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          .pos shouldBe Vector2(0, 0)
        coordinator.updateSystems()
        coordinator.getEntityComponent(entity, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          .pos shouldBe Vector2(0, 1)
        coordinator.getEntityComponent(entity, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
          .vel shouldBe 0
      }
      "move multiple entities to direction" in {
        coordinator.addEntity(entity2)
        coordinator.addEntityComponent(entity2, pos2)
        coordinator.addEntityComponent(entity2, dir2)
        coordinator.addEntityComponent(entity2, vel2)
        coordinator.getEntityComponent(entity, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
          .vel = 3
        coordinator.updateSystems()
        coordinator.getEntityComponent(entity2, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          .pos shouldBe Vector2(0, -2)
        coordinator.getEntityComponent(entity, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          .pos shouldBe Vector2(0, 4)

      }

    }
  }

}
