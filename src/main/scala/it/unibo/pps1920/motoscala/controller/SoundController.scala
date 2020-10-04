package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent

trait SoundController {
  def redirectSoundEvent(me: MediaEvent): Unit
}
