package it.unibo.pps1920.motoscala.engine

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameLoopTest extends AnyWordSpec with Matchers with BeforeAndAfter with Eventually {

  import org.scalatest.time.{Millis, Span}


  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(750, Millis)), interval = scaled(Span(5, Millis)))
  val engine: Engine = GameEngine()
  var loop: GameCycle = _

  before {
    loop = GameLoop(60, engine)
  }

  "A GameLoop" when {
    "created" should {
      "be stopped" in {
        assert(loop.status == GameStatus.Stopped)
      }
    }
    "started" should {
      "be running" in {
        loop.start()
        eventually {
          assert(loop.status == GameStatus.Running)
        }
      }
      "be pausable" in {
        loop.start()
        eventually {

          assert(loop.pause() == GameStatus.Paused)
        }
      }
      "be stoppable" in {
        loop.start()
        eventually {

          assert(loop.halt() == GameStatus.Stopped)
        }
      }
    }
    "paused" should {
      "be resumable" in {
        loop.start()
        loop.pause()
        eventually {
          assert(loop.unPause() == GameStatus.Running)
        }
      }
    }


  }
}
