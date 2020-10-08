package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.{VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.util.Direction
import it.unibo.pps1920.motoscala.ecs.util.Direction._
import it.unibo.pps1920.motoscala.engine.CommandQueue
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner
import it.unibo.pps1920.motoscala.ecs.util.Vector2


@RunWith(classOf[JUnitRunner])
class InputSystemTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {


  var coordinator: Coordinator = _
  var input: System = _

  val eventQueue: CommandQueue = CommandQueue()
  val entity: TestEntity = TestEntity(UUID.randomUUID())
  val entity2: TestEntity = TestEntity(UUID.randomUUID())


  val vel: VelocityComponent = VelocityComponent(Vector2(0,0), Vector2(20,20))
  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    input = InputSystem(coordinator, eventQueue)
    coordinator.registerComponentType(classOf[VelocityComponent])
    coordinator.registerSystem(input)
    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, vel)

  }

  "A movementSystem" when {
    "updating" should {
      "move an entity to direction" in {
        eventQueue.enqueue(CommandEvent(CommandData(entity, North)))
        coordinator.updateSystems()
        val vVec = coordinator.getEntityComponent(entity, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
          .vel
          Direction.velToDir(vVec) shouldBe North
      }

    }
  }
}
