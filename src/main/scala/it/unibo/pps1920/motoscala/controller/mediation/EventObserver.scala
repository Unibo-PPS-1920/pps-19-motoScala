package it.unibo.pps1920.motoscala.controller.mediation

/**
 * An Observer of events.
 *
 * @tparam T the type of event observed.
 */
trait EventObserver[T] {
  /**
   * Notify the observer with the event.
   *
   * @param event the notified event.
   */
  def notify(event: T): Unit
}