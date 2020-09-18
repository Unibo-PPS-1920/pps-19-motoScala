package it.unibo.pps1920.motoscala.engine

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameLoopTest extends AnyWordSpec with Matchers with BeforeAndAfter with Eventually {

  val engine: Engine = GameEngine()
  var loop: GameCycle = _

  before {
    loop = GameLoop(60, engine)
  }

  "A GameCycle" when {
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
          loop.pause()
          assert(loop.status == GameStatus.Paused)
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
