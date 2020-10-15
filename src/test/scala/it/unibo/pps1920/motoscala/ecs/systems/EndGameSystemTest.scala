package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.{EntityData, LevelEndData}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.EndData
import it.unibo.pps1920.motoscala.controller.mediation.{Displayable, EventData, Mediator}
import it.unibo.pps1920.motoscala.controller.{Controller, EngineController}
import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity, RedPupaEntity}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.systems.EndGameSystemTestClasses.Display
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
  var display: Display = _
  val pid: UUID = UUID.randomUUID()
  val entity: BumperCarEntity = BumperCarEntity(pid)
  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    controller = Controller()
    display = new Display()
    mediator = controller.mediator
    controller.mediator.subscribe(display)
    endsys = EndGameSystem(coordinator, mediator, Vector2(20, 20), GameEngine(controller, UUID.randomUUID()))
    val pos: PositionComponent = PositionComponent(Vector2(1, 2))
    val shape = ShapeComponent(Circle(3))
    val v = VelocityComponent(Vector2(0, -10))
    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[ShapeComponent])
    coordinator.registerComponentType(classOf[VelocityComponent])
    coordinator.registerSystem(endsys)

    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity, v)
    coordinator.addEntityComponent(entity, pos)
    coordinator.addEntityComponent(entity, shape)

  }
  override def afterAll(): Unit = {

  }

  "A endgame" when {
    "updating" should {
      "emit a winning event when only a bumpercar is left" in {
        endsys.update()
        res.event shouldBe EndData(hasWon = true, entity)
      }
      "eliminate enemies" in {
        val e = RedPupaEntity(UUID.randomUUID())
        val pos: PositionComponent = PositionComponent(Vector2(-1, -1))
        val shape = ShapeComponent(Circle(3))
        val v = VelocityComponent(Vector2(0, -10))
        coordinator.addEntity(e)
        coordinator.addEntityComponent(e, v)
        coordinator.addEntityComponent(e, pos)
        coordinator.addEntityComponent(e, shape)
        endsys.entitiesRef() shouldBe Set(entity, e)
        endsys.update()
        endsys.entitiesRef() shouldBe Set(entity)

      }
      "emit a loss event when an entity goes out of the playing field" in {
        coordinator.getEntityComponent(entity, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
          .pos = Vector2(-1, -1)
        endsys.update()
        res.event shouldBe EndData(hasWon = false, entity)
        endsys.entitiesRef() shouldBe Set.empty
      }

    }
  }

}

object EndGameSystemTestClasses {
  final class Display extends Displayable {
    override def notifyDrawEntities(player: EntityData,
                                    entities: Set[EntityData]): Unit = ???
    override def notifyLevelSetup(data: EventData.LevelSetupData): Unit = ???
    override def notifyLevelEnd(data: LevelEndData): Unit = res.event = data
  }

}

object res {
  var event: Any = _
}

