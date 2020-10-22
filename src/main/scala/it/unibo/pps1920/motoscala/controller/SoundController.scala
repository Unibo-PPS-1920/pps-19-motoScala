package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent

/** Represents the controller that manage sounds, using one [[it.unibo.pps1920.motoscala.controller.managers.audio.SoundAgent]].
 * The sound controller use the SoundAgent for handle all the MediaEvent incoming.
 */
protected[controller] trait SoundController {
  /** Represents the controller that manage sounds, using one [[it.unibo.pps1920.motoscala.controller.managers.audio.SoundAgent]].
   *
   * @Param the media event to be handled
   */
  def redirectSoundEvent(me: MediaEvent): Unit
}
