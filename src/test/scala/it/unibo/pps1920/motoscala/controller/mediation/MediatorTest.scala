package it.unibo.pps1920.motoscala.controller.mediation

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event._
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.util.Direction.{North, South}
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
        mediator.publishEvent(Event.CommandEvent(CommandData(TestEntity(UUID.randomUUID()), South, moving = true)))
        mediator.publishEvent(Event.DrawEntityEvent(List.empty))
        mediator.publishEvent(Event.LevelSetupEvent(""))
        mediator.publishEvent(Event.LevelEndEvent(""))
      }
    }
    "publishing" should {
      "set flags true" in {
        ToggleFlags.cmdFlag shouldBe true
        ToggleFlags.drawFlag shouldBe true
        ToggleFlags.setupFlag shouldBe true
        ToggleFlags.endFlag shouldBe true
      }
    }
    "unregistering observer" in {
      mediator unsubscribe observerCommand
    }
    "publishing" in {
      mediator.publishEvent(Event.CommandEvent(CommandData(TestEntity(UUID.randomUUID()), North, moving = true)))
    }
    "not modify flag" in {
      ToggleFlags.cmdFlag shouldBe true
    }
  }
}
object MediatorTestClasses {
  final class DisplayableImpl extends Displayable {
    override def notifyDrawEntities(entities: Seq[Event.EntityData]): Unit = ToggleFlags.drawFlag = !ToggleFlags
      .drawFlag
    override def notifyLevelSetup(data: LevelSetupData): Unit = ToggleFlags.setupFlag = !ToggleFlags.setupFlag
    override def notifyLevelEnd(data: LevelEndData): Unit = ToggleFlags.endFlag = !ToggleFlags.endFlag
  }
  final class CommandableImpl extends Commandable {
    override def notifyCommand(cmd: CommandData): Unit = ToggleFlags.cmdFlag = !ToggleFlags.cmdFlag
  }
}
object ToggleFlags {
  var drawFlag = false
  var setupFlag = false
  var endFlag = false
  var cmdFlag = false
}
case class TestEntity(id: UUID) extends Entity {
  override def uuid: UUID = id
}
