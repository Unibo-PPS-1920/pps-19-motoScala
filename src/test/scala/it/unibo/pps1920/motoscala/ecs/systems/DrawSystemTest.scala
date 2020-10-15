package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.EventData.DrawEntityData
import it.unibo.pps1920.motoscala.controller.mediation.{Event, Mediator}
import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.util.{Direction, Vector2}
import it.unibo.pps1920.motoscala.ecs.{Entity, System}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DrawSystemTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {

  import DrawSystemTestClasses._

  var coordinator: Coordinator = _
  var drawSystem: System = _
  var mediator: Mediator = _
  val pid = UUID.randomUUID()
  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    mediator = new MediatorImpl()
    drawSystem = DrawSystem(mediator, coordinator, pid)
    val pos: PositionComponent = PositionComponent(Vector2(1, 2))
    val shape = ShapeComponent(Circle(3))
    val v: VelocityComponent = VelocityComponent(Vector2(0, -10), Vector2(20, 20))

    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[ShapeComponent])
    coordinator.registerComponentType(classOf[VelocityComponent])
    coordinator.registerSystem(drawSystem)

    val entity = TestEntity(pid)
    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, v)
    coordinator.addEntityComponent(entity, pos)
    coordinator.addEntityComponent(entity, shape)
  }
  override def afterAll(): Unit = {

  }

  "A drawSystem" when {
    "updating" should {
      "emit the correct event" in {
        drawSystem.update()
        resulta.event shouldBe Event.DrawEntityEvent(DrawEntityData(Vector2(1, 2), Direction
          .North, Circle(3), TestEntity(pid)), Set())
      }
      "emit the correct event for multiple entities" in {
        val entity2id = UUID.randomUUID()
        val entity2 = TestEntity(entity2id)
        coordinator.addEntity(entity2)
        val pos2: PositionComponent = PositionComponent(Vector2(3, 2))
        val shape2 = ShapeComponent(Circle(2))
        val vel2 = VelocityComponent(Vector2(0, -5), Vector2(20, 20))
        coordinator.addEntityComponent(entity2, pos2)
        coordinator.addEntityComponent(entity2, shape2)
        coordinator.addEntityComponent(entity2, vel2)
        drawSystem.update()
        resulta
          .event shouldBe Event.DrawEntityEvent(DrawEntityData(Vector2(1, 2), Direction
          .North, Circle(3), TestEntity(pid)), Set(DrawEntityData(Vector2(3, 2), Direction
          .North, Circle(2), TestEntity(entity2id))))
      }
    }
  }

}

object DrawSystemTestClasses {


  final class MediatorImpl extends Mediator {

    import it.unibo.pps1920.motoscala.controller.mediation.EventObserver

    import scala.reflect.ClassTag

    override def subscribe[T: ClassTag](observer: EventObserver[T]*): Unit = {}

    override def unsubscribe[T](observer: EventObserver[T]*): Unit = {}

    override def publishEvent[T: ClassTag](ev: T): Unit = resulta.event = ev

  }

}

object resulta {
  var event: Any = _
}

case class TestEntity(id: UUID) extends Entity {
  override def uuid: UUID = id
}
