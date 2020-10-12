package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.System
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AISystemTest extends AnyWordSpec with BeforeAndAfterAll with Matchers {
  var ai: System = _
  var c: Coordinator = _
  override def beforeAll(): Unit = {
    c = Coordinator()
    ai = AISystem(c)
  }
  "an ai System" when {
    "updating" in {
      ai.update()
    }
  }
}
