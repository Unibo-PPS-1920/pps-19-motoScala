package it.unibo.pps1920.motoscala.controller.managers.audio

trait SoundController {
  def playSoundTrack(media: Media): Unit
  def setVolumeSoundTrack(value: Double): Unit
  def setVolumeEffect(value: Double): Unit
  def stopMusic(): Unit
  def restartMusic(): Unit
  def pauseMusic(): Unit
}