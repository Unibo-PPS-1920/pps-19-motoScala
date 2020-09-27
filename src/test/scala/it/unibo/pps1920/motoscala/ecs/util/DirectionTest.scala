package it.unibo.pps1920.motoscala.ecs.util

import it.unibo.pps1920.motoscala.ecs.util.Direction._
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DirectionTest extends AnyWordSpec with Matchers {

  "North" when {
    "summed to Center" should {
      "be North" in {
        North + Center shouldBe North
      }
    }
    "summed to North" should {
      "be North" in {
        North + North shouldBe North
      }
    }
    "summed to West" should {
      "be NorthWest" in {
        North + West shouldBe NorthWest
      }
    }
    "summed to South" should {
      " be Center" in {
        North + South shouldBe Center
      }
    }
    "summed to East" should {
      "be NorthEast" in {
        North + East shouldBe NorthEast
      }
    }
    "summed to NorthEast" should {
      " be NorthEast" in {
        North + NorthEast shouldBe NorthEast
      }
    }
    "summed to NorthWest" should {
      "be North" in {
        North + NorthWest shouldBe NorthWest
      }
    }
    "summed to SouthEast" should {
      " be East" in {
        North + SouthEast shouldBe East
      }
    }
    "summed to SouthWest" should {
      "be North" in {
        North + SouthWest shouldBe West
      }
    }

  }
}
