package it.unibo.pps1920.motoscala

import it.unibo.pps1920.motoscala.controller.SoundController
import it.unibo.pps1920.motoscala.controller.mediation.Mediator

trait EngineController extends SoundController {
  def mediator: Mediator
}
