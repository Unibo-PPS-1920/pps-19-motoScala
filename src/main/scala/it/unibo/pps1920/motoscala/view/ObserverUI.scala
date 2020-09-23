package it.unibo.pps1920.motoscala.view

import it.unibo.pps1920.motoscala.view.events.ViewEvent

trait ObserverUI {
  def notify(ev: ViewEvent): Unit
}
