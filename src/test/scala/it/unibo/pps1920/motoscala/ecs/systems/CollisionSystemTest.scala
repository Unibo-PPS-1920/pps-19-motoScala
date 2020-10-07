package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, PositionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.util.Direction.{North, South}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CollisionSystemTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {

/*
  var coordinator: Coordinator = _
  var movement: System = _
  var collision: System = _
  var mediator: Mediator = _
  val entity: BumperCarEntity = BumperCarEntity(UUID.randomUUID())
  val entity2: Enemy1Entity = Enemy1Entity(UUID.randomUUID())
  val pos: PositionComponent = PositionComponent(Vector2(5, 5))
  val dir: DirectionComponent = DirectionComponent(South)
  val vel: VelocityComponent = VelocityComponent(1)
  val coll: CollisionComponent = CollisionComponent(1)
  val pos2: PositionComponent = PositionComponent(Vector2(5, 5))
  val dir2: DirectionComponent = DirectionComponent(North)
  val vel2: VelocityComponent = VelocityComponent(1)
  val coll2: CollisionComponent = CollisionComponent(1)

  override def beforeAll(): Unit = {
    coordinator = Coordinator()

    movement = MovementSystem(coordinator)
    collision = CollisionSystem(coordinator, fps = 60)

    coordinator.registerComponentType(classOf[PositionComponent])
      .registerComponentType(classOf[VelocityComponent])
      .registerComponentType(classOf[DirectionComponent])
      .registerComponentType(classOf[CollisionComponent])
      .registerSystem(movement)
      .registerSystem(collision)
      .addEntity(entity)
      .addEntityComponent(entity, pos)
      .addEntityComponent(entity, dir)
      .addEntityComponent(entity, vel)
      .addEntityComponent(entity, coll)
      .addEntity(entity2)
      .addEntityComponent(entity2, pos2)
      .addEntityComponent(entity2, dir2)
      .addEntityComponent(entity2, vel2)
      .addEntityComponent(entity2, coll2)

  }

  "A collisionSystem" when {
    "updating" should {
      "handle collisions" in {
        coordinator.updateSystems()
        coordinator.getEntityComponent(entity, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          .pos shouldBe Vector2(5, 6)
        coordinator.getEntityComponent(entity2, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          .pos shouldBe Vector2(5, 4)
      }
    }
  }

*/
}
