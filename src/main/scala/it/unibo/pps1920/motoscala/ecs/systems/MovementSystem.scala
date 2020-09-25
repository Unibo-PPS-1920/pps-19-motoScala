package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.AbstractSystem
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, PositionComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator

trait MovementSystem extends AbstractSystem {}

object MovementSystem {

  def apply(coordinator: Coordinator): MovementSystem = new MovementSystemImpl(coordinator)

  private class MovementSystemImpl(coordinator: Coordinator) extends MovementSystem {


    override def update(): Unit = entitiesRef()
      .foreach(e => {
        val p = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]

        val v = coordinator.getEntityComponent(e, classOf[VelocityComponent]).get.asInstanceOf[VelocityComponent]

        val d = coordinator.getEntityComponent(e, classOf[DirectionComponent]).get.asInstanceOf[DirectionComponent]

        p.pos = p.pos add (d.dir.value mul v.vel)
        v.vel = 0
        //        coordinator.getEntityComponent(e, classOf[VelocityComponent]).asInstanceOf[VelocityComponent].vel = 0
      })
  }

}

object Main extends App {
  val num = 5
  var num2 = num
  num * 5

  print(num)
}
