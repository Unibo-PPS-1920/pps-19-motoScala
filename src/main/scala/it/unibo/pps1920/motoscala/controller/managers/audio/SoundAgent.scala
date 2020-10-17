package it.unibo.pps1920.motoscala.controller.managers.audio

import java.util.concurrent.ArrayBlockingQueue

import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.loadFromJar
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
  cacheSounds()
  private val blockingQueue: ArrayBlockingQueue[MediaEvent] = new ArrayBlockingQueue[MediaEvent](QUEUE_SIZE)
  private var actualMusicPlayer: Option[MediaPlayer] = None
  private var actualClipPlayer: AudioClip = _
  private var volumeMusic: Double = 0.5
  private var volumeEffect: Double = 1.0

  override def run(): Unit = {
    while (true) {
      Try(this.blockingQueue.take()) match {
        case Success(ev) => ev.handle(this)
        case Failure(exception) => logger warn exception.getMessage
      }
    }
  }
  override def playMusic(media: Music): Unit = {
    if (!this.medias.contains(media)) {
      this.medias += (media -> new MediaPlayer(new Media(loadFromJar(media.entryName))))
    }
    Platform.runLater(() => {
      this.actualMusicPlayer = this.medias.get(media)
      this.actualMusicPlayer.foreach(_.setVolume(volumeMusic))
      this.actualMusicPlayer.foreach(_.play())
      this.actualMusicPlayer.foreach(_.setCycleCount(MediaPlayer.INDEFINITE))
      this.medias.filterNot(p => p._2 == this.actualMusicPlayer.get).foreach(_._2.pause())
    })
  }
  override def stopMusic(): Unit = Platform.runLater(() => this.actualMusicPlayer.foreach(_.stop()))
  override def pauseMusic(): Unit = Platform.runLater(() => this.actualMusicPlayer.foreach(_.pause()))
  override def resumeMusic(): Unit = Platform.runLater(() => this.actualMusicPlayer.foreach(_.play()))
  override def playClip(clip: Clips): Unit = {
    if (!this.clips.contains(clip)) {
      this.clips += (clip -> new AudioClip(loadFromJar(clip.entryName)))
    }
    Platform.runLater(() => {
      this.actualClipPlayer = this.clips(clip)
      this.actualClipPlayer.play(volumeEffect)
    })
  }
  override def setVolumeMusic(value: Double): Unit = {
    this.volumeMusic = value
    Platform.runLater(() => {this.actualMusicPlayer.foreach(_.setVolume(this.volumeMusic)) })
  }
  override def setVolumeEffect(value: Double): Unit = {
    this.volumeEffect = value
    Platform.runLater(() => {this.actualClipPlayer.setVolume(this.volumeEffect) })
  }
  override def restartMusic(): Unit = {
    Platform.runLater(() => {
      this.actualMusicPlayer.foreach(_.stop())
      this.actualMusicPlayer.foreach(_.seek(Duration.ZERO))
      this.actualMusicPlayer.foreach(_.play())
    })
  }
  override def enqueueEvent(ev: MediaEvent): Unit = Platform.runLater(() => {this.blockingQueue.add(ev) })

  private def cacheSounds(): Unit = {
    //Increase loading speed with cache
    this.medias += (Music.Home -> new MediaPlayer(new Media(loadFromJar(Music.Home.entryName))))
    this.medias += (Music.Game -> new MediaPlayer(new Media(loadFromJar(Music.Game.entryName))))
    this.medias(Music.Home).play()
    this.medias(Music.Home).stop()
    this.medias(Music.Game).play()
    this.medias(Music.Game).stop()
  }
}

object SoundAgent {
  def apply(): SoundAgent = new ConcreteSoundAgent
}
