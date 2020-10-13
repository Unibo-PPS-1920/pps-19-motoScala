package it.unibo.pps1920.motoscala.ecs.util

import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class Vector2Test extends AnyWordSpec with Matchers {

  "Vector2Test" should {
    "add a vector" in {
      Vector2(1, 1) add Vector2(1, 1) shouldBe Vector2(2, 2)
    }

    "add a scalar" in {
      Vector2(1, 1) add 1 shouldBe Vector2(2, 2)
    }

    "div" in {
      Vector2(2, 2) div Vector2(2, 2) shouldBe Vector2(1, 1)
    }

    "unit" in {
      Vector2(5, 6).clip() shouldBe Vector2(1, 1)
      Vector2(0, 4).clip() shouldBe Vector2(0, 1)
      Vector2(-5, -5).clip() shouldBe Vector2(-1, -1)
    }

    "mul" in {
      Vector2(3, 4) mul 2 shouldBe Vector2(6, 8)
    }
    "dist" in {
      Vector2(0, 0) dist Vector2(1, 1) shouldBe math.sqrt(2)
      Vector2(1, 1) dist Vector2(0, 0) shouldBe math.sqrt(2)
      Vector2(0, 0) dist Vector2(2, 1) shouldBe math.sqrt(5)
      Vector2(2, 1) dist Vector2(0, 0) shouldBe math.sqrt(5)
    }
  }
}
