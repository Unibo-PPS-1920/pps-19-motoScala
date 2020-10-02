package it.unibo.pps1920.motoscala.engine

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.model.Level.{Coordinate, LevelData}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.wordspec.AnyWordSpec

class GameEngineTest extends AnyWordSpec with BeforeAndAfterAll {
  var engine: Engine = _
  val mediator: Mediator = Mediator()

  override def beforeAll(): Unit = {
    engine = GameEngine(mediator, UUID.randomUUID())
  }

  "A GameEngine" when {
    "created" should {
      "init" in {
        engine.init(LevelData(1, Coordinate(1, 1), List()))
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

