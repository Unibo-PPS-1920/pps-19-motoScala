package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.EventData.EndData
import it.unibo.pps1920.motoscala.controller.mediation.{Event, Mediator}
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, PositionComponent, ShapeComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity, RedPupaEntity}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.util.{Direction, Vector2}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EndGameSystemTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {

  import EndGameSystemTestClasses._

  var coordinator: Coordinator = _
  var endsys: System = _
  var mediator: Mediator = _
  val pid = UUID.randomUUID()
  val entity = BumperCarEntity(pid)
  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    mediator = new MediatorImpl()
    endsys = EndGameSystem(coordinator, mediator, Vector2(20, 20))
    val pos: PositionComponent = PositionComponent(Vector2(1, 2))
    val shape = ShapeComponent(Circle(3))
    val d = DirectionComponent(Direction.North)
    val v = VelocityComponent(2)
    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[ShapeComponent])
    coordinator.registerComponentType(classOf[DirectionComponent])
    coordinator.registerComponentType(classOf[VelocityComponent])
    coordinator.registerSystem(endsys)

    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, v)
    coordinator.addEntityComponent(entity, pos)
    coordinator.addEntityComponent(entity, shape)
    coordinator.addEntityComponent(entity, d)

  }
  override def afterAll(): Unit = {

  }

  "A endgame" when {
    "updating" should {

      "emit a winning event when only a bumpercar is left" in {
        endsys.update()
        res.event shouldBe Event.LevelEndEvent(EndData(true, entity))
      }
      "eliminate enemies" in {
        val e = RedPupaEntity(UUID.randomUUID())
        val pos: PositionComponent = PositionComponent(Vector2(-1, -1))
        val shape = ShapeComponent(Circle(3))
        val d = DirectionComponent(Direction.North)
        val v = VelocityComponent(2)
        coordinator.addEntity(e)
        coordinator.addEntityComponent(e, v)
        coordinator.addEntityComponent(e, pos)
        coordinator.addEntityComponent(e, shape)
        coordinator.addEntityComponent(e, d)
        endsys.entitiesRef() shouldBe Set(entity, e)
        endsys.update()
        endsys.entitiesRef() shouldBe Set(entity)

      }
      "emit a loss event when an entity goes out of the playing field" in {
        coordinator.getEntityComponent(entity, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          .pos = Vector2(-1, -1)
        endsys.update()
        res.event shouldBe Event.LevelEndEvent(EndData(false, entity))
        endsys.entitiesRef() shouldBe Set.empty
      }

    }
  }

}

object EndGameSystemTestClasses {


  final class MediatorImpl extends Mediator {

    import it.unibo.pps1920.motoscala.controller.mediation.EventObserver

    import scala.reflect.ClassTag

    override def subscribe[T: ClassTag](observer: EventObserver[T]*): Unit = {}

    override def unsubscribe[T](observer: EventObserver[T]*): Unit = {}

    override def publishEvent[T: ClassTag](ev: T): Unit = res.event = ev

  }

}

object res {
  var event: Any = _
}

