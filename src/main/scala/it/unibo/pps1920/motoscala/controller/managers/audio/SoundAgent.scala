package it.unibo.pps1920.motoscala.controller.managers.audio

import java.util.concurrent.ArrayBlockingQueue

import javafx.application.Platform
import javafx.scene.media.{AudioClip, Media, MediaPlayer}
import javafx.util.Duration
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

trait SoundAgent extends Thread with SoundAgentLogic {
  def enqueueEvent(ev: MediaEvent): Unit
}

private final class ConcreteSoundAgent extends SoundAgent {
  final val QUEUE_SIZE = 100
  private val logger = LoggerFactory getLogger classOf[ConcreteSoundAgent]
  private var clips: Map[Clips, AudioClip] = Map()
  private var medias: Map[Music, MediaPlayer] = Map()
  private var blockingQueue: ArrayBlockingQueue[MediaEvent] = new ArrayBlockingQueue[MediaEvent](QUEUE_SIZE)
  private var actualMusicPlayer: MediaPlayer = _
  private var actualClipPlayer: AudioClip = _
  private var volumeMusic: Double = 1.0
  private var volumeEffect: Double = 1.0

  override def run(): Unit = {
    while (true) {
      Try(this.blockingQueue.take()) match {
        case Success(ev) => ev.handle(this)
        case Failure(exception) => logger warn (exception.getMessage)
      }
    }
  }
  override def playMusic(media: Music): Unit = {
    if (!this.medias.contains(media)) {
      this.medias += (media -> (new MediaPlayer(new Media(this.getClass.getResource(media.entryName).toString))))
    }
    Platform.runLater(() => {
      this.actualMusicPlayer = this.medias(media)
      this.actualMusicPlayer.setVolume(volumeMusic)
      this.actualMusicPlayer.play()
      this.actualMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE)
    })

  }
  override def stopMusic(): Unit = this.actualMusicPlayer.stop()
  override def playClip(clip: Clips): Unit = {
    if (!this.clips.contains(clip)) {
      this.clips += (clip -> (new AudioClip(this.getClass.getResource(clip.entryName).toString)))
    }

    Platform.runLater(() => {
      this.actualClipPlayer = this.clips(clip)
      this.actualClipPlayer.play(volumeEffect)
    })
  }
  override def setVolumeMusic(value: Double): Unit = {
    this.volumeMusic = value
    Platform.runLater(() => {
      this.actualMusicPlayer.setVolume(this.volumeMusic)
    })
  }
  override def setVolumeEffect(value: Double): Unit = {
    this.volumeEffect = value
    Platform.runLater(() => {
      this.actualClipPlayer.setVolume(this.volumeEffect)
    })
  }
  override def restartMusic(): Unit = {
    Platform.runLater(() => {
      this.actualMusicPlayer.stop()
      this.actualMusicPlayer.seek(Duration.ZERO);
      this.actualMusicPlayer.play()
    })
  }
  override def pauseMusic(): Unit = Platform.runLater(() => {
    this.actualMusicPlayer.pause()
  })

  override def enqueueEvent(ev: MediaEvent): Unit = Platform.runLater(() => {
    this.blockingQueue.add(ev)

  })

  override def resumeMusic(): Unit = Platform.runLater(() => {
    this.actualMusicPlayer.pause()
  })
}

object SoundAgent {
  def apply(): SoundAgent = new ConcreteSoundAgent
}
