package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.DrawEntityEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.DrawEntityData
import it.unibo.pps1920.motoscala.controller.mediation.{EntityType, Mediator}
import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, PositionComponent, ShapeComponent, TypeComponent}
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
  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    mediator = new MediatorImpl()
    drawSystem = DrawSystem(mediator, coordinator)
    val pos: PositionComponent = PositionComponent(Vector2(1, 2))
    val shape = ShapeComponent(Circle(3))
    val t = TypeComponent(EntityType.Player)
    val d = DirectionComponent(Direction.North)
    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[ShapeComponent])
    coordinator.registerComponentType(classOf[TypeComponent])
    coordinator.registerComponentType(classOf[DirectionComponent])
    coordinator.registerSystem(drawSystem)
    val entity = TestEntity(UUID.randomUUID())
    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, pos)
    coordinator.addEntityComponent(entity, shape)
    coordinator.addEntityComponent(entity, t)
    coordinator.addEntityComponent(entity, d)

  }
  override def afterAll(): Unit = {

  }

  "A drawSystem" when {
    "updating" should {
      "emit the correct event" in {
        drawSystem.update()
        result.event shouldBe DrawEntityEvent(Seq(DrawEntityData(Vector2(1, 2), Direction.North, Circle(3), EntityType
          .Player)))
      }
      "emit the correct event for multiple entities" in {
        val entity2 = TestEntity(UUID.randomUUID())
        coordinator.addEntity(entity2)
        val pos2: PositionComponent = PositionComponent(Vector2(3, 2))
        val shape2 = ShapeComponent(Circle(2))
        val d2 = DirectionComponent(Direction.North)
        val t2 = TypeComponent(EntityType.Enemy1)
        coordinator.addEntityComponent(entity2, pos2)
        coordinator.addEntityComponent(entity2, shape2)
        coordinator.addEntityComponent(entity2, t2)
        coordinator.addEntityComponent(entity2, d2)
        drawSystem.update()
        result
          .event shouldBe DrawEntityEvent(Seq(DrawEntityData(Vector2(1, 2), Direction.North, Circle(3), EntityType
          .Player), DrawEntityData(Vector2(3, 2), Direction.North, Circle(2), EntityType.Enemy1)))
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

    override def publishEvent[T: ClassTag](ev: T): Unit = result.event = ev

  }

}

object result {
  var event: Any = _
}

case class TestEntity(id: UUID) extends Entity {
  override def uuid: UUID = id
}
