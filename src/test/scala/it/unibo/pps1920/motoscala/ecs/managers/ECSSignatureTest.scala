package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.managers.ECSSignatureTestClasses._
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ECSSignatureTest extends AnyWordSpec with BeforeAndAfterAll with Matchers {
  private var signature: ECSSignature = _
  private var comp1: Comp1 = _
  private var comp2: Comp2 = _

  override def beforeAll(): Unit = {
    signature = ECSSignature()
    comp1 = Comp1()
    comp2 = Comp2()
  }

  "An ECSSignature" when {
    "created" should {
      "sign component" in {
        signature.signComponent(comp1.getClass, comp2.getClass) shouldBe ECSSignature(comp1.getClass, comp2.getClass)
      }
      "return the signature" in {
        signature.signatureSet shouldBe Set(comp1.getClass, comp2.getClass)
      }
      "repudiate component" in {
        signature.repudiateComponent(comp1.getClass) shouldBe ECSSignature(comp2.getClass)
      }
      "repudiate all components" in {
        signature.repudiateComponent(Set(comp2.getClass)) shouldBe ECSSignature()
      }
      "add component set" in {
        signature.signComponent(Set(comp1.getClass, comp2.getClass)).signatureSet shouldBe Set(comp1.getClass, comp2
          .getClass)
      }
    }
  }
}

object ECSSignatureTestClasses {
  final case class Comp1() extends Component
  final case class Comp2() extends Component
}