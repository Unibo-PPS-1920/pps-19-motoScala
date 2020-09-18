package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.controller.mediation.Event._
/**
 * Contract dedicated to listening for Commandable events
 */
trait Commandable extends EventObserver[CommandableEvent] {
  def notifyCommand(cmd: CommandData): Unit
  override def notify(event: CommandableEvent): Unit = event match {
    case CommandEvent(cmd) => notifyCommand(cmd)
    case _ =>
  }
}
