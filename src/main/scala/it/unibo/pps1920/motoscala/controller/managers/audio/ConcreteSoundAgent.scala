package it.unibo.pps1920.motoscala.controller.managers.audio

import java.nio.file.Path
import java.util.concurrent.ArrayBlockingQueue

import org.slf4j.LoggerFactory
import scalafx.scene.media.{AudioClip, MediaPlayer}

import scala.util.{Failure, Success, Try}

private final class ConcreteSoundAgent extends Thread with SoundAgentLogic with SoundAgent {

  private val logger = LoggerFactory getLogger classOf[ConcreteSoundAgent]
  private var clips: Map[Clips, AudioClip] = Map()
  private var medias: Map[Media, MediaPlayer] = Map()
  private var blockingQueue: ArrayBlockingQueue[MediaEvent] = new ArrayBlockingQueue[MediaEvent](100)
  private var actualMusicPlayer: Option[MediaPlayer] = None
  private var actualClipPlayer: Option[AudioClip] = None
  private var volumeMusic: Double = 1.0
  private var volumeEffect: Double = 1.0

  override def run(): Unit = {
    while (true) {
      Try(this.blockingQueue.take()) match {
        case Success(value) =>
        case Failure(exception) => logger warn (exception.getMessage)
      }
    }
  }

  override def playMusic(media: Media): Unit = {
    require(medias.isDefinedAt(media))
    val musicPlayer: MediaPlayer = medias(media)
    musicPlayer.setVolume(volumeMusic)
    musicPlayer.play()
    this.stopMusic()
    this.actualMusicPlayer = Some(musicPlayer)
  }
  override def stopMusic(): Unit = this.actualClipPlayer.foreach(_.stop())
  override def playClip(clip: Clips): Unit = {
    require(clips.isDefinedAt(clip))
    val clipPlayer: AudioClip = clips(clip)
    clipPlayer.setVolume(volumeEffect)
    clipPlayer.play()
    this.actualClipPlayer = Some(clipPlayer)
  }
  override def setVolumeMusic(value: Double): Unit = this.volumeMusic = value
  override def setVolumeEffect(value: Double): Unit = this.volumeEffect = value
  override def restartMusic(): Unit = this.actualClipPlayer.foreach(_.stop())

  override def pauseMusic(): Unit = this.actualClipPlayer.foreach(_.stop())

  override def enqueueEvent(ev: MediaEvent): Unit = this.blockingQueue.add(ev)

  override def setClips(clips: Map[Media, Path]): Unit = {
    ???
  }
  override def setMediaPath(medias: Map[Media, Path]): Unit = {
    ???
  }

}

object ConcreteSoundAgent {
  def apply(): ConcreteSoundAgent = new ConcreteSoundAgent
}
