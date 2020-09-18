package it.unibo.pps1920.motoscala.controller.mediation

trait EventObserver[T] {
  def notify(event: T): Unit
}