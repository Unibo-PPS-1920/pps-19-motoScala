package it.unibo.pps1920.motoscala.controller.managers.audio

trait SoundAgentLogic {
  def playMusic(media: Media): Unit
  def playClip(clip: Clips): Unit
  def setVolumeMusic(value: Double): Unit
  def setVolumeEffect(value: Double): Unit
  def stopMusic(): Unit
  def restartMusic(): Unit
  def pauseMusic(): Unit
}
