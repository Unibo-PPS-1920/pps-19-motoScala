package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.mediation.Mediator

trait EngineController extends SoundController {
  def mediator: Mediator
}
