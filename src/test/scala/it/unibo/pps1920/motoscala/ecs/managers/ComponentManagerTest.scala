package it.unibo.pps1920.motoscala.ecs.managers

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.{Component, Entity}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ComponentManagerTest extends AnyWordSpec with BeforeAndAfterAll with Matchers {
  import ComponentManagerTestClasses._
  private var componentManager: ComponentManager = _
  private var entity: Entity = _
  private var entityNotUsed: Entity = _
  private var comp1: Comp1 = _
  private var comp2: Comp2 = _

  override def beforeAll(): Unit = {
    componentManager = ComponentManager()
    entity = TestEntity(UUID.randomUUID())
    entityNotUsed = TestEntity(UUID.randomUUID())
    comp1 = Comp1()
    comp2 = Comp2()
  }

  override def afterAll(): Unit = {
  }

  "A ComponentManager" when {
    "created" should {
      "register a component type" in {
        componentManager.registerComponentType(comp1.getClass)
      }
      "check if component is registered" in {
        componentManager.checkRegisteredComponent(comp1.getClass) shouldBe true
        componentManager.checkRegisteredComponent(comp2.getClass) shouldBe false
      }
      "register the same component" in {
        componentManager.registerComponentType(comp1.getClass)
      }
    }
    "a component is registered" should {
      "bind component to entity" in {
        componentManager.bindComponentToEntity(entity, comp1) shouldBe ECSSignature(comp1.getClass)
      }
      "get component from entity" in {
        componentManager.getEntityComponent(entity, comp1.getClass).get shouldEqual comp1
      }
      "unbind component from entity" in {
        componentManager.unbindComponentFromEntity(entity, comp1) shouldBe ECSSignature()
      }
      "unbind nothing from entity" in {
        componentManager.unbindComponentFromEntity(entity, comp1) shouldBe ECSSignature()
      }
    }
    "a component is not registered" should {
      "fail trying to bind component" in {
        assertThrows[IllegalArgumentException] {
          componentManager.bindComponentToEntity(entity, comp2)
        }
      }
    }
  }
}
object ComponentManagerTestClasses {
  final case class Comp1() extends Component
  final case class Comp2() extends Component
  final case class TestEntity(_uuid: UUID) extends Entity {
    override def uuid: UUID = _uuid
  }
}
