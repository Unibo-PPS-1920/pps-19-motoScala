package it.unibo.pps1920.motoscala.engine

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameLoopTest extends AnyWordSpec with Matchers with BeforeAndAfter with Eventually {

  import org.scalatest.time.{Millis, Span}


  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(750, Millis)), interval = scaled(Span(5, Millis)))
  val engine: Engine = GameEngine()
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
      "be stoppable" in {
        loop.start()
        eventually {
          loop.halt() shouldBe GameStatus.Stopped
        }
        eventually {
          loop.pause() shouldBe GameStatus.Stopped
        }
      }
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