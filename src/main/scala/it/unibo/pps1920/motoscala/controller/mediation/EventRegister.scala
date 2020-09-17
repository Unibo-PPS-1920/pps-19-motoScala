package it.unibo.pps1920.motoscala.controller.mediation

trait EventRegister {
  def registerHandler[T <: Event](handler: EventHandler[T]): Unit
  def unRegisterHandler[T <: Event](handler: EventHandler[T]): Unit
}
