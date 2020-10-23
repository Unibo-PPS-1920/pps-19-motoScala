package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.mediation.Mediator

/** Represents the controller that manage the [[it.unibo.pps1920.motoscala.engine.Engine]].
 * It provide the [[it.unibo.pps1920.motoscala.controller.mediation.Mediator]] for the Engine
 */
trait EngineController extends SoundController {
  /** The mediator getter. */
  def mediator: Mediator
}
