package it.unibo.pps1920.motoscala.ecs.managers

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Component, Entity, System}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SystemManagerTest extends AnyWordSpec with BeforeAndAfterAll with Matchers {
  import SystemManagerTestClasses._
  private var sysManager: SystemManager = _
  private var entity: Entity = _
  private var entity2: Entity = _
  private var comp1: Comp1 = _
  private var comp2: Comp2 = _
  private var sys1: System = _
  private var sys2: System = _

  override def beforeAll(): Unit = {
    sysManager = SystemManager()
    entity = TestEntity(UUID.randomUUID())
    entity2 = TestEntity(UUID.randomUUID())
    comp1 = Comp1()
    comp2 = Comp2()
    sys1 = System1()
    sys2 = System2()
  }

  "A SystemManager" when {
    "created" should {
      "register a new system" in {
        sysManager.registerSystem(sys1)
        sysManager.registerSystem(sys2)
      }

      "notify entity signature changed" in {
        sysManager.entitySignatureChanged(entity, ECSSignature(comp1.getClass, comp2.getClass)) shouldBe Set(sys1)
        sysManager.entitySignatureChanged(entity, ECSSignature(comp1.getClass)) shouldBe Set(sys2, sys1)
      }

      "update all systems" in {
        sysManager.updateAll()
      }

      "entity destroyed" in {
        sysManager.entityDestroyed(entity)
      }
    }
  }
}
object SystemManagerTestClasses {
  final case class Comp1() extends Component
  final case class Comp2() extends Component
  final case class TestEntity(_uuid: UUID) extends Entity {
    override def uuid: UUID = _uuid
  }
  final case class System1() extends AbstractSystem(ECSSignature(classOf[Comp1], classOf[Comp2])) {
    override def update(): Unit = println("system1")
  }
  final case class System2() extends AbstractSystem(ECSSignature(classOf[Comp1])) {
    override def update(): Unit = println("system2")
  }
}
