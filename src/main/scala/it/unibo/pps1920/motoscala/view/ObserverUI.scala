package it.unibo.pps1920.motoscala.view

import it.unibo.pps1920.motoscala.view.events.ViewEvent

/** The View Observer. Part of the Observer Pattern */
trait ObserverUI {
  /**
   * Called when the observer is notified by the [[it.unibo.pps1920.motoscala.controller.ObservableUI]].
   *
   * @param ev the view event
   */
  def notify(ev: ViewEvent): Unit
}
