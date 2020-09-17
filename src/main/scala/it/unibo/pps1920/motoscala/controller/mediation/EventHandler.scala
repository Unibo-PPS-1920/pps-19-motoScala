package it.unibo.pps1920.motoscala.controller.mediation

trait EventHandler[-T <: Event] {
  def handle(ev: T): Unit
}

sealed trait Event
case class UpdateEvent(val k: Int = 2) extends Event
case class DrawEvent(val k: Int = 5) extends Event