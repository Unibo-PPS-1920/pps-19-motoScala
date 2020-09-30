package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandEvent, CommandableEvent}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.util.Direction.{North, South}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class InputSystemTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {


  var coordinator: Coordinator = _
  var input: System = _

  val eventQueue: mutable.Queue[CommandableEvent] = mutable.Queue()
  val entity = TestEntity(UUID.randomUUID())
  val entity2 = TestEntity(UUID.randomUUID())

  val dir: DirectionComponent = DirectionComponent(South)
  val vel: VelocityComponent = VelocityComponent(0)

  val dir2: DirectionComponent = DirectionComponent(North)
  val vel2: VelocityComponent = VelocityComponent(2)
  override def beforeAll(): Unit = {
    coordinator = Coordinator()


    input = InputSystem(coordinator, eventQueue)


    coordinator.registerComponentType(classOf[VelocityComponent])
    coordinator.registerComponentType(classOf[DirectionComponent])
    coordinator.registerSystem(input)
    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, dir)
    coordinator.addEntityComponent(entity, vel)

  }

  "A movementSystem" when {
    "updating" should {
      "move an entity to direction" in {
        eventQueue.enqueue(CommandEvent(CommandData(entity, North, true)))
        coordinator.updateSystems()
        coordinator.getEntityComponent(entity, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]
          .vel shouldBe 1
        coordinator.getEntityComponent(entity, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]
          .dir shouldBe North
      }

    }
  }
}
