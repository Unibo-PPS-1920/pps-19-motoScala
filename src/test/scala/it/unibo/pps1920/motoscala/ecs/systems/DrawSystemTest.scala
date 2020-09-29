package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.DrawEntityEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.EntityData
import it.unibo.pps1920.motoscala.controller.mediation.{EntityType, Mediator}
import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent, TypeComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
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
    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[ShapeComponent])
    coordinator.registerComponentType(classOf[TypeComponent])
    coordinator.registerSystem(drawSystem)
    coordinator
      .signSystem(drawSystem, ECSSignature.apply()
        .signComponent(classOf[PositionComponent], classOf[ShapeComponent], classOf[TypeComponent]))
    val entity = TestEntity(UUID.randomUUID())
    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, pos)
    coordinator.addEntityComponent(entity, shape)
    coordinator.addEntityComponent(entity, t)

  }
  override def afterAll(): Unit = {

  }

  "A drawSystem" when {
    "updating" should {
      "emit the correct event" in {
        drawSystem.update()
        result.event shouldBe DrawEntityEvent(Seq(EntityData(Vector2(1, 2), Circle(3), EntityType.Player))
                                              )
      }
      "emit the correct event for multiple entities" in {
        val entity2 = TestEntity(UUID.randomUUID())
        coordinator.addEntity(entity2)
        val pos2: PositionComponent = PositionComponent(Vector2(3, 2))
        val shape2 = ShapeComponent(Circle(2))

        val t2 = TypeComponent(EntityType.Enemy1)
        coordinator.addEntityComponent(entity2, pos2)
        coordinator.addEntityComponent(entity2, shape2)
        coordinator.addEntityComponent(entity2, t2)
        drawSystem.update()
        result
          .event shouldBe DrawEntityEvent(Seq(EntityData(Vector2(1, 2), Circle(3), EntityType
          .Player), EntityData(Vector2(3, 2), Circle(2), EntityType.Enemy1)))
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
