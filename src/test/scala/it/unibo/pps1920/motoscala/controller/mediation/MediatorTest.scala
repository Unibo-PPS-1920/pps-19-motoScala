package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.controller.mediation.Event._
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MediatorTest extends AnyWordSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {
  import org.slf4j.LoggerFactory
  val logger = LoggerFactory.getLogger(classOf[MediatorTest])
  var mediator: Mediator = _
  var observerDrawable: DisplayableImpl = _
  var observerCommand: CommandableImpl = _

  override def beforeAll: Unit = {
    mediator = Mediator()
    observerDrawable = new DisplayableImpl()
    observerCommand = new CommandableImpl()
  }
  override def afterAll: Unit = {
    logger info "Mediator test finished"
  }

  "A Mediator" when {
    "created" should {
      "allow observer to subscribe" in {
        mediator subscribe observerDrawable
        mediator subscribe observerCommand
      }
      "allow to send command" in {
        mediator.publishEvent(new Event.CommandEvent("Ciao"))
        mediator.publishEvent(new Event.DrawEntityEvent(List.empty))
        mediator.publishEvent(new Event.LevelSetupEvent("Bebebe"))
        mediator.publishEvent(new Event.LevelEndEvent("Bababa"))
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
      mediator.publishEvent(new Event.CommandEvent("Ciao"))
    }
    "not modify flag" in {
      ToggleFlags.cmdFlag shouldBe true
    }
  }

  class DisplayableImpl extends Displayable {
    override def notifyDrawEntities(entities: Seq[Entity]): Unit = ToggleFlags.drawFlag = !ToggleFlags.drawFlag
    override def notifyLevelSetup(data: LevelSetupData): Unit = ToggleFlags.setupFlag = !ToggleFlags.setupFlag
    override def notifyLevelEnd(data: LevelEndData): Unit = ToggleFlags.endFlag = !ToggleFlags.endFlag
  }
  class CommandableImpl extends Commandable {
    override def notifyCommand(cmd: CommandData): Unit = ToggleFlags.cmdFlag = !ToggleFlags.cmdFlag
  }
}

object ToggleFlags {
  var drawFlag = false
  var setupFlag = false
  var endFlag = false
  var cmdFlag = false
}

