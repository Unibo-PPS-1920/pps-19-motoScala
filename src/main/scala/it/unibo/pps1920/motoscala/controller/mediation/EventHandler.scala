package it.unibo.pps1920.motoscala.controller.mediation

trait EventHandler[T <: Event] {
  def handle(ev: T): Unit
}

trait Event {

}
