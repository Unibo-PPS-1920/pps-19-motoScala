package it.unibo.pps1920.motoscala.controller.mediation

import scala.reflect.ClassTag

/**
 * An observable object that can be observed by many [[EventObserver]].
 * Observers can register, and unregister to eventSubject.
 * Event must be published to the Subject which convey them to dedicated observers
 */
trait EventSubject {
  /**
   * Add [[EventObserver]] to the subject.
   *
   * @param observer the observer
   * @tparam T type of the Event to be observed
   */
  def subscribe[T: ClassTag](observer: EventObserver[T]*): Unit
  /**
   * Remove [[EventObserver]] from the subject.
   *
   * @param observer the observer
   * @tparam T type of the Event to be observed
   */
  def unsubscribe[T](observer: EventObserver[T]*): Unit
  /**
   * Publish an event to all dedicated [[EventObserver]].
   *
   * @param ev the event
   * @tparam T type of the Event to be observed
   */
  def publishEvent[T: ClassTag](ev: T): Unit
}