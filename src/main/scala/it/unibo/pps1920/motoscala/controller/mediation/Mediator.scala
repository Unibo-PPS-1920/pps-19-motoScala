package it.unibo.pps1920.motoscala.controller.mediation

trait MediatorLike extends EventRegister {

}

class Mediator private() extends MediatorLike {
  override def registerHandler[T <: Event](handler: EventHandler[T]): Unit = ???
  override def unRegisterHandler[T <: Event](handler: EventHandler[T]): Unit = ???
}

object Mediator {
  def apply(): Mediator = new Mediator()
}
