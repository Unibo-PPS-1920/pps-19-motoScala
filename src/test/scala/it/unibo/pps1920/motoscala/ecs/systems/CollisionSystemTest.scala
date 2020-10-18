package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.{Displayable, EventData, Mediator}
import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EntityData, LevelEndData}
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.systems.CollisionSystemTestClasses.EngineControllerMock
import it.unibo.pps1920.motoscala.ecs.components.{CollisionComponent, JumpComponent, PositionComponent, ShapeComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity, RedPupaEntity}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.util.Direction.{North, South}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CollisionSystemTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {


  var coordinator: Coordinator = _
  var movement: System = _
  var collision: System = _
  var mediator: Mediator = _
  var controller: EngineController = _

  val player: BumperCarEntity = BumperCarEntity(UUID.randomUUID())
  val playerShape: ShapeComponent = ShapeComponent(Circle(5))
  val playerPos: PositionComponent = PositionComponent(Vector2(100, 100))
  val playerVel: VelocityComponent = VelocityComponent(currentVel = Vector2(10, 0), inputVel = Vector2(10, 0))
  val playerColl: CollisionComponent = CollisionComponent(5)
  val playerJump: JumpComponent = JumpComponent()
  val pupa: RedPupaEntity = RedPupaEntity(UUID.randomUUID())
  val pupaPos: PositionComponent = PositionComponent(Vector2(108, 100))
  val pupaVel: VelocityComponent = VelocityComponent(currentVel= Vector2(-5,0), inputVel = Vector2(-5,0))
  val pupaColl: CollisionComponent = CollisionComponent(10)

  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    mediator = Mediator()
    movement = MovementSystem(coordinator)
    controller = new EngineControllerMock(mediator)
    collision = CollisionsSystem(coordinator, controller, fps = 60)

    coordinator.registerComponentType(classOf[PositionComponent])
      .registerComponentType(classOf[VelocityComponent])
      .registerComponentType(classOf[JumpComponent])
      .registerComponentType(classOf[CollisionComponent])
      .registerComponentType(classOf[ShapeComponent])
      .registerSystem(movement)
      .registerSystem(collision)
      .addEntity(player)
      .addEntityComponent(player, playerPos)
      .addEntityComponent(player, playerShape)
      .addEntityComponent(player, playerVel)
      .addEntityComponent(player, playerColl)
      .addEntityComponent(player, playerJump)
      .addEntity(pupa)
      .addEntityComponent(pupa, pupaPos)
      .addEntityComponent(pupa, pupaVel)
      .addEntityComponent(pupa, pupaColl)

  }

  "A collisionSystem" when {
    "updating" should {
      "handle collisions" in {
        coordinator.updateSystems()
        coordinator.updateSystems()
        coordinator.getEntityComponent(player, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
          .currentVel shouldBe Vector2(10, 0)
        coordinator.getEntityComponent(pupa, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
          .currentVel shouldBe Vector2(-5, 0)
      }
    }
  }


}
object CollisionSystemTestClasses {
  final class EngineControllerMock(_mediator: Mediator) extends EngineController {
    override def mediator: Mediator = _mediator
    override def redirectSoundEvent(me: MediaEvent): Unit = {}
  }
}
