package it.unibo.pps1920.motoscala.controller.managers.audio

import java.util.concurrent.ArrayBlockingQueue

import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.loadFromJarToString
import javafx.scene.media.{AudioClip, Media, MediaPlayer}
import javafx.util.Duration
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}
/** A specific sound agent, that extends [[Thread]], this Thread, should be used for play all [[Clips]] and [[Music]]
 * the agent should use one safe Queue, for handle the external event.
 * */
sealed trait SoundAgent extends Thread with SoundAgentLogic {
  private final val QueueSize = 1000
  protected val blockingQueue: ArrayBlockingQueue[MediaEvent] = new ArrayBlockingQueue[MediaEvent](QueueSize)
  /** Enqueue the event to be handled from the agent.
   *
   * @param ev the event.
   * */
  def enqueueEvent(ev: MediaEvent): Unit
}

private final class ConcreteSoundAgent extends SoundAgent {
  private val logger = LoggerFactory getLogger classOf[ConcreteSoundAgent]
  private var clips: Map[Clips, AudioClip] = Map()
  private var medias: Map[Music, MediaPlayer] = Map()
  private var actualMusicPlayer: Option[MediaPlayer] = None
  private var actualClipPlayer: Option[AudioClip] = None
  private var volumeMusic: Double = 0.5
  private var volumeEffect: Double = 1.0
  cacheSounds()

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
      this.medias += (media -> new MediaPlayer(new Media(loadFromJarToString(media.entryName))))
    }
    this.actualMusicPlayer = this.medias.get(media)
    this.actualMusicPlayer.foreach(_.setVolume(volumeMusic))
    this.actualMusicPlayer.foreach(_.play())
    this.actualMusicPlayer.foreach(_.setCycleCount(MediaPlayer.INDEFINITE))
    this.medias.filterNot(p => p._2 == this.actualMusicPlayer.get).foreach(_._2.pause())
  }
  override def stopMusic(): Unit = this.actualMusicPlayer.foreach(_.stop())
  override def pauseMusic(): Unit = this.actualMusicPlayer.foreach(_.pause())
  override def resumeMusic(): Unit = this.actualMusicPlayer.foreach(_.play())
  override def playClip(clip: Clips): Unit = {
    if (!this.clips.contains(clip)) {
      this.clips += (clip -> new AudioClip(loadFromJarToString(clip.entryName)))
    }
    this.actualClipPlayer = Some(this.clips(clip))
    this.actualClipPlayer.get.play(volumeEffect)
  }
  override def setVolumeMusic(value: Double): Unit = {
    this.volumeMusic = if (value < 0.05) 0.0 else value
    this.actualMusicPlayer.foreach(_.setVolume(this.volumeMusic))
  }
  override def setVolumeEffect(value: Double): Unit = {
    this.volumeEffect = if (value < 0.05) 0.0 else value
    this.actualClipPlayer.foreach(_.setVolume(this.volumeEffect))
  }
  override def restartMusic(): Unit = {
    this.actualMusicPlayer.foreach(_.stop())
    this.actualMusicPlayer.foreach(_.seek(Duration.ZERO))
    this.actualMusicPlayer.foreach(_.play())
  }
  override def enqueueEvent(ev: MediaEvent): Unit = this.blockingQueue.add(ev)

  private def cacheSounds(): Unit = {
    //Increase loading speed with cache
    this.medias += (Music.Home -> new MediaPlayer(new Media(loadFromJarToString(Music.Home.entryName))))
    this.medias += (Music.Game -> new MediaPlayer(new Media(loadFromJarToString(Music.Game.entryName))))
    this.medias(Music.Home).play()
    this.medias(Music.Home).stop()
    this.medias(Music.Game).play()
    this.medias(Music.Game).stop()
  }
}
/** Factory for [[SoundAgent]] instances. */
object SoundAgent {
  /** Creates a new [[SoundAgent]] */
  def apply(): SoundAgent = new ConcreteSoundAgent
}
