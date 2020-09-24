package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.AbstractSystem

trait MovementSystem extends AbstractSystem{

}

object MovementSystem {
  def apply(): MovementSystem = new MovementSystemImpl()
  private class MovementSystemImpl extends MovementSystem{
    override def update(): Unit = ???
  }

}
