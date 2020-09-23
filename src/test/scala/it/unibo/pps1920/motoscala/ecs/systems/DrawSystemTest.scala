package it.unibo.pps1920.motoscala.ecs.systems

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.managers.ECSSignature
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import it.unibo.pps1920.motoscala.controller.mediation.Event.DrawEntityEvent
import it.unibo.pps1920.motoscala.ecs.Entity
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DrawSystemTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {

  import scalafx.scene.paint.Color
  import DrawSystemTestClasses._
  var coordinator: Coordinator = _
  var drawSystem: DrawSystem = _
  var mediator: Mediator= _
  override def beforeAll(): Unit = {
    coordinator = Coordinator()
    mediator = new MediatorImpl()
    drawSystem = DrawSystem(mediator,coordinator)
    val pos: PositionComponent = PositionComponent(1, 2, 3)
    val shape = ShapeComponent(Circle((0, 0), 3), Color(1, 1, 1, 1))
    val entity = TestEntity(UUID.randomUUID())
    coordinator.registerComponentType(classOf[PositionComponent])
    coordinator.registerComponentType(classOf[ShapeComponent])
    coordinator.registerSystem(drawSystem)
    coordinator.signSystem(drawSystem,ECSSignature.apply().signComponent(classOf[PositionComponent],classOf[ShapeComponent]))
    coordinator.addEntity(entity)
    coordinator.addEntityComponent(entity,pos)
    coordinator.addEntityComponent(entity,shape)

  }
  override def afterAll(): Unit = {

  }

  "A drawSystem" when {
    "updating" should {
      "emit the correct event" in {
        drawSystem.update()
        result.event shouldBe DrawEntityEvent(Seq(((1, 2, 3), Circle((0, 0), 3), Color(1, 1, 1, 1))))
      }
    }
  }

}

object DrawSystemTestClasses {


  final class MediatorImpl extends Mediator {

    import it.unibo.pps1920.motoscala.controller.mediation.EventObserver

    import scala.reflect.ClassTag

    override def subscribe[T: ClassTag](observer: EventObserver[T]*): Unit = {}

    override def unsubscribe[T](observer: EventObserver[T]*): Unit = {}

    override def publishEvent[T: ClassTag](ev: T): Unit = result.event = ev

  }
}

object result {
  var event:Any=_
}

case class TestEntity(id:UUID)extends Entity {
  override def uuid: UUID = id
}
