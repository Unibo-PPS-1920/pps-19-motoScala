package it.unibo.pps1920.motoscala.controller.mediation

import scala.reflect.ClassTag

trait EventSubject {
  def subscribe[T: ClassTag](observer: EventObserver[T]): Unit
  def unsubscribe[T](observer: EventObserver[T]): Unit
  def publishEvent[T: ClassTag](ev: T): Unit
}