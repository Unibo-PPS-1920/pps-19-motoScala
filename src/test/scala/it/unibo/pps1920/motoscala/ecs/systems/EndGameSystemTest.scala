package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EndData, EntityData, LifeData, SoundEvent}
import it.unibo.pps1920.motoscala.controller.mediation.{Displayable, EventData, Mediator}
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.core.Coordinator
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity, RedPupaEntity}
import it.unibo.pps1920.motoscala.ecs.systems.EndGameSystemTestClasses.{DisplayMock, EngineControllerMock}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.engine.GameEngine
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EndGameSystemTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {
  var coordinator: Coordinator = _
  var endsys: System = _
  var mediator: Mediator = _
  var controller: EngineController = _
  var display: DisplayMock = _
  val pid: UUID = UUID.randomUUID()
  val entity: BumperCarEntity = BumperCarEntity(pid)
  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    controller = new EngineControllerMock(Mediator())
    display = new DisplayMock()
    mediator = controller.mediator
    controller.mediator.subscribe(display)
    endsys = EndGameSystem(coordinator, mediator, Vector2(20, 20), GameEngine(controller, List(BumperCarEntity(UUID
                                                                                                                 .randomUUID()))))
    val pos: PositionComponent = PositionComponent(Vector2(1, 2))
    val shape = ShapeComponent(Circle(3))
    val v = VelocityComponent(Vector2(0, -10))
    val s = ScoreComponent(1)
    val c = CollisionComponent(10, 20)
    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[ShapeComponent])
    coordinator.registerComponentType(classOf[VelocityComponent])
    coordinator.registerComponentType(classOf[ScoreComponent])
    coordinator.registerComponentType(classOf[CollisionComponent])
    coordinator.registerSystem(endsys)

    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, s)
    coordinator.addEntityComponent(entity, v)
    coordinator.addEntityComponent(entity, pos)
    coordinator.addEntityComponent(entity, shape)
    coordinator.addEntityComponent(entity, c)

  }
  override def afterAll(): Unit = {

  }

  "A endgame" when {
    "updating" should {
      "emit a winning event when only a bumpercar is left" in {
        endsys.update()
        res.event shouldBe EventData.EndData(hasWon = true, entity, 0)
      }
      "eliminate enemies" in {
        val e = RedPupaEntity(UUID.randomUUID())
        val pos: PositionComponent = PositionComponent(Vector2(-1, -1))
        val shape = ShapeComponent(Circle(3))
        val v = VelocityComponent(Vector2(0, -10))
        val s = ScoreComponent(1)
        val c = CollisionComponent(10, 10)
        coordinator.addEntity(e)
        coordinator.addEntityComponent(e, v)
        coordinator.addEntityComponent(e, pos)
        coordinator.addEntityComponent(e, shape)
        coordinator.addEntityComponent(e, s)
        coordinator.addEntityComponent(e, c)
        endsys.entitiesRef() shouldBe Set(entity, e)
        endsys.update()
        endsys.entitiesRef() shouldBe Set(entity)

      }
      "emit a loss event when an entity goes out of the playing field" in {
        coordinator.getEntityComponent[PositionComponent](entity).pos = Vector2(-1, -1)
        endsys.update()
        res.event shouldBe EventData.EndData(hasWon = false, entity, 1)
        endsys.entitiesRef() shouldBe Set.empty
      }

    }
  }

}

object EndGameSystemTestClasses {
  final class DisplayMock extends Displayable {
    override def notifyDrawEntities(player: Set[Option[EntityData]],
                                    entities: Set[EntityData]): Unit = {}
    override def notifyLevelEnd(data: EndData): Unit = res.event = data
    override def notifyEntityLife(data: LifeData): Unit = {}
    override def notifyRedirectSound(event: SoundEvent): Unit = {}
  }
  final class EngineControllerMock(_mediator: Mediator) extends EngineController {
    override def mediator: Mediator = _mediator
  }
}

object res {
  var event: Any = _
}

