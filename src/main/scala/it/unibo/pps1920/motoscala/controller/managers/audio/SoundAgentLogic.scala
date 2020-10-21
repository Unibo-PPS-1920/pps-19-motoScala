package it.unibo.pps1920.motoscala.controller.managers.audio

/** The public logic of the sound Agent, the sound agent handle music and audio clip. */
trait SoundAgentLogic {
  /** Play the actual media passed.
   *
   * @param media the [[Music]] to be played.
   * */
  def playMusic(media: Music): Unit
  /** Play the actual media passed
   *
   * @param clip the [[Clips]] to be played.
   * */
  def playClip(clip: Clips): Unit
  /** Change the volume for all media [[Music]].
   *
   * @param value the volume to be set
   * */
  def setVolumeMusic(value: Double): Unit
  /** Change the volume for all [[Clips]].
   *
   * @param value volume value to be set.
   * */
  def setVolumeEffect(value: Double): Unit
  /** Stop the actual music. */
  def stopMusic(): Unit
  /** Restart the actual music. */
  def restartMusic(): Unit
  /** Pause the actual music. */
  def pauseMusic(): Unit
  /** Resume the actual music. */
  def resumeMusic(): Unit
}
