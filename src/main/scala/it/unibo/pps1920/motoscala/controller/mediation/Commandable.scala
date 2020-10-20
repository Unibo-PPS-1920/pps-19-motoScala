package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.controller.mediation.Event._

/**
 * This interfaced wraps an [[EventObserver]] for [[CommandableEvent]].
 */
trait Commandable extends EventObserver[CommandableEvent] {
  def notifyCommand(cmd: CommandData): Unit
  override def notify(event: CommandableEvent): Unit = event match {
    case CommandEvent(cmd) => notifyCommand(cmd)
  }
}
