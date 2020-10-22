package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.controller.mediation.Event._

/** This interfaced wraps an [[EventObserver]] for [[it.unibo.pps1920.motoscala.controller.mediation.Event.CommandableEvent]]. */
trait Commandable extends EventObserver[CommandableEvent] {

  /** Notify the observer with data from [[it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent]].
   *
   * @param data the data containing command information
   */
  def notifyCommand(data: CommandData): Unit

  override def notify(event: CommandableEvent): Unit = event match {
    case CommandEvent(cmd) => notifyCommand(cmd)
  }
}
