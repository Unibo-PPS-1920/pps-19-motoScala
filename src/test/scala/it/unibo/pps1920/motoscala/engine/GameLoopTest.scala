package it.unibo.pps1920.motoscala.engine

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EndData, EntityData, LifeData, SoundEvent}
import it.unibo.pps1920.motoscala.controller.mediation.{Displayable, Mediator}
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.ecs.systems.res
import it.unibo.pps1920.motoscala.engine.GameLoopTestClasses.EngineControllerMock
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Span}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameLoopTest extends AnyWordSpec with Matchers with BeforeAndAfter with Eventually {
  implicit override val patienceConfig: PatienceConfig = {

    PatienceConfig(timeout = scaled(Span(10000, Millis)), interval = scaled(Span(5, Millis)))

  }
  val controller: EngineController = new EngineControllerMock(Mediator())
  val engine: Engine = GameEngine(controller, List(BumperCarEntity(UUID.randomUUID())), 1)
  var loop: GameLoop = _

  before {
    loop = GameLoop(60, engine)
  }

  "A GameLoop" when {
    "created" should {
      "be stopped" in {
        loop.status shouldBe GameStatus.Stopped
      }
      "not be pausable" in {
        loop.pause() shouldBe GameStatus.Stopped
      }
      "not be unpausable" in {
        loop.unPause() shouldBe GameStatus.Stopped
      }
      "not be stoppable" in {
        loop.halt() shouldBe GameStatus.Stopped
      }
    }
    "started" should {
      "be running" in {
        loop.start()
        eventually {
          loop.status shouldBe GameStatus.Running
        }
      }
      "be pausable" in {
        loop.start()
        eventually {
          loop.pause() shouldBe GameStatus.Paused
        }
        eventually {
          loop.pause() shouldBe GameStatus.Paused
        }
      }
      //      "be stoppable" in {
      //        loop.start()
      //        eventually {
      //          loop.halt() shouldBe GameStatus.Stopped
      //        }
      //        eventually {
      //          loop.pause() shouldBe GameStatus.Stopped
      //        }
      //      }
    }
    "paused" should {
      "be resumable" in {
        loop.start()
        eventually {
          loop.pause() shouldBe GameStatus.Paused
        }
        eventually {
          loop.unPause() shouldBe GameStatus.Running
        }
      }
    }
  }
}

object GameLoopTestClasses {
  final class DisplayMock extends Displayable {
    override def notifyDrawEntities(player: Set[Option[EntityData]],
                                    entities: Set[EntityData]): Unit = {}
    override def notifyLevelEnd(data: EndData): Unit = res.event = data
    override def notifyEntityLife(data: LifeData): Unit = {}
    override def execute(runnable: Runnable): Unit = runnable.run()
  }
  final class EngineControllerMock(_mediator: Mediator) extends EngineController {
    override def mediator: Mediator = _mediator
    override def redirectSoundEvent(me: SoundEvent): Unit = {}
  }
}
