package it.unibo.pps1920.motoscala.controller.managers.audio

import java.nio.file.Path

trait SoundAgent {
  def enqueueEvent(ev: MediaEvent): Unit
  def setClips(clips: Map[Media, Path]): Unit
  def setMediaPath(medias: Map[Media, Path]): Unit
}
