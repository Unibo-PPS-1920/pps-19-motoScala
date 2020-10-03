package it.unibo.pps1920.motoscala.controller.managers.audio

trait MediaEvent {
  def handle(action: SoundAgentLogic => Unit): Unit
}

