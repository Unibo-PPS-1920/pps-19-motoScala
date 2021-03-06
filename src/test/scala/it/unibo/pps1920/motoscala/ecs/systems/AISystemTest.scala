package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.{AIComponent, PositionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.core.Coordinator
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity, RedPupaEntity}
import it.unibo.pps1920.motoscala.ecs.util.Direction.{North, South}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.engine.CommandQueue
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class AISystemTest extends AnyWordSpec with BeforeAndAfterAll with Matchers {
  var ai: System = _
  var coordinator: Coordinator = _
  var q: CommandQueue = _
  val pid: UUID = UUID.randomUUID()
  val eid: UUID = UUID.randomUUID()
  val eid2: UUID = UUID.randomUUID()
  val p: BumperCarEntity = BumperCarEntity(pid)
  val e: RedPupaEntity = RedPupaEntity(eid)
  val e2: RedPupaEntity = RedPupaEntity(eid2)
  val pos: PositionComponent = PositionComponent(Vector2(0, 0))
  val vel: VelocityComponent = VelocityComponent(Vector2(0, 20), Vector2(20, 20))
  val pos2: PositionComponent = PositionComponent(Vector2(0, 10))
  val vel2: VelocityComponent = VelocityComponent(Vector2(20, 0), Vector2(20, 20))
  val aic: AIComponent = AIComponent(foolishness = 0, targets = mutable.Stack(p))
  val pos3: PositionComponent = PositionComponent(Vector2(0, 10))
  val vel3: VelocityComponent = VelocityComponent(Vector2(20, 0), Vector2(20, 20))
  val aic2: AIComponent = AIComponent(foolishness = 0, targets = mutable.Stack(p))
  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    q = CommandQueue()
    ai = AISystem(coordinator, q, skipFrames = 1)
    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[VelocityComponent])
    coordinator.registerComponentType(classOf[AIComponent])
    coordinator.registerSystem(ai)
    coordinator.addEntity(p)
    coordinator.addEntityComponent(p, pos)
    coordinator.addEntityComponent(p, vel)
    coordinator.addEntity(e)
    coordinator.addEntityComponent(e, pos2)
    coordinator.addEntityComponent(e, vel2)
    coordinator.addEntityComponent(e, aic)
    coordinator.addEntity(e2)
    coordinator.addEntityComponent(e2, pos3)
    coordinator.addEntityComponent(e2, vel3)
    coordinator.addEntityComponent(e2, aic2)
  }
  "an aiSystem" should {
    "make the controlled entity go north" when {
      "updating" in {
        coordinator.updateSystems()
        val ev = q.dequeueAll()
        ev.head.cmd.direction shouldBe North
      }
    }
    "make the controlled entity go south" when {
      "updating" in {

        pos.pos = Vector2(0, 20)
        coordinator.updateSystems()
        val ev = q.dequeueAll()
        ev.head.cmd.direction shouldBe South
      }
    }
    "go to the corner if there are no more players" when {
      "updating" in {
        pos2.pos shouldBe Vector2(0, 10)
        coordinator.removeEntity(p)
        coordinator.updateSystems()

        val ev = q.dequeueAll()
        ev.head.cmd.direction shouldBe North
      }
    }

  }
}
