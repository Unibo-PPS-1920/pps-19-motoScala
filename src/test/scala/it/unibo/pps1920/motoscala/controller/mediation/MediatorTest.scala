package it.unibo.pps1920.motoscala.controller.mediation

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event._
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.ecs.util.Direction.{North, South}
import it.unibo.pps1920.motoscala.model.Level.{Coordinate, LevelData}
import it.unibo.pps1920.motoscala.view.events.ViewEvent._
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatestplus.junit.JUnitRunner
import org.slf4j.LoggerFactory

@RunWith(classOf[JUnitRunner])
class MediatorTest extends AnyWordSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {
  import MediatorTestClasses._
  private val logger = LoggerFactory.getLogger(classOf[MediatorTest])
  private var mediator: Mediator = _
  private var observerDrawable: DisplayableImpl = _
  private var observerCommand: CommandableImpl = _

  override def beforeAll(): Unit = {
    mediator = Mediator()
    observerDrawable = new DisplayableImpl()
    observerCommand = new CommandableImpl()
  }
  override def afterAll(): Unit = {
    logger info "Mediator test finished"
  }

  "A Mediator" when {
    "created" should {
      "allow observer to subscribe" in {
        mediator subscribe observerDrawable
        mediator subscribe observerCommand
      }
      "allow to send command" in {
        mediator.publishEvent(Event.CommandEvent(CommandData(TestEntity(UUID.randomUUID()), South)))
        mediator.publishEvent(Event.DrawEntityEvent(null, Set.empty))
        mediator.publishEvent(LevelSetupEvent(LevelSetupData(
          LevelData(0, Coordinate(0, 0), List()), isSinglePlayer = true, isHosting = true,
          BumperCarEntity(UUID.randomUUID()))))
        mediator.publishEvent(Event.LevelEndEvent(EventData.EndData(hasWon = true, null, 0)))
      }
    }
    "publishing" should {
      "set flags true" in {
        ToggleFlags.cmdFlag shouldBe true
        ToggleFlags.drawFlag shouldBe true
        ToggleFlags.endFlag shouldBe true
      }
    }
    "unregistering observer" in {
      mediator unsubscribe observerCommand
    }
    "publishing" in {
      mediator.publishEvent(Event.CommandEvent(CommandData(TestEntity(UUID.randomUUID()), North)))
    }
    "not modify flag" in {
      ToggleFlags.cmdFlag shouldBe true
    }
  }
}
object MediatorTestClasses {
  final class DisplayableImpl extends Displayable {
    override def notifyDrawEntities(player: Set[Option[EntityData]], entities: Set[Event.EntityData]): Unit =
      ToggleFlags.drawFlag = !ToggleFlags.drawFlag
    override def notifyLevelEnd(data: EndData): Unit = ToggleFlags.endFlag = !ToggleFlags.endFlag
    override def notifyEntityLife(data: LifeData): Unit = {}
    override def notifyRedirectSound(event: SoundEvent): Unit = {}
  }
  final class CommandableImpl extends Commandable {
    override def notifyCommand(cmd: CommandData): Unit = ToggleFlags.cmdFlag = !ToggleFlags.cmdFlag
  }
}
object ToggleFlags {
  var drawFlag = false
  var endFlag = false
  var cmdFlag = false
}
case class TestEntity(id: UUID) extends Entity {
  override def uuid: UUID = id
}
