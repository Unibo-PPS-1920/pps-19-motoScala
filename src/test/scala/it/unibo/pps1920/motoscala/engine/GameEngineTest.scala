package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.model.Level.LevelData
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameEngineTest extends AnyWordSpec with BeforeAndAfterAll {
  var engine: Engine = _
  val mediator: Mediator = Mediator()

  override def beforeAll(): Unit = {
    engine = GameEngine(mediator)
  }

  "A GameEngine" when {
    "created" should {
      "init" in {
        engine.init(LevelData(1, (1, 1), List()))
      }
      "start" in {
        engine.start()
      }
    }
    "started" should {
      "not start again" in {
        eventually {

          engine.start()
        }
      }
      "pause" in {
        eventually {
          engine.pause()
        }
      }
      "resume" in {
        eventually {
          engine.resume()
        }
      }
      "stop" in {
        eventually {
          engine.stop()
        }
      }
    }
  }
}

