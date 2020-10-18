package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.mediation.Mediator

trait EngineController {
  def mediator: Mediator
}
