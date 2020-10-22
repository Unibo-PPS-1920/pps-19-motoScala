package it.unibo.pps1920.motoscala.controller.managers.audio

import java.util.concurrent.ArrayBlockingQueue

import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.loadFromJarToString
import javafx.scene.media.{AudioClip, Media, MediaPlayer}
import javafx.util.Duration
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/** A specific sound agent, that extends [[java.lang.Thread]], this Thread, should be used for play all [[Clips]] and [[Music]]
 * the agent should use one safe Queue, for handle the external event.
 * */
sealed trait SoundAgent extends Thread with SoundAgentLogic {
  private final val QueueSize = 1000
  protected val blockingQueue: ArrayBlockingQueue[MediaEvent] = new ArrayBlockingQueue[MediaEvent](QueueSize)
  /** Enqueue the event to be handled from the agent.
   *
   * @param ev the event
   * */
  def enqueueEvent(ev: MediaEvent): Unit
}

private final class ConcreteSoundAgent extends SoundAgent {
  import MagicValues._

  private val logger = LoggerFactory getLogger classOf[ConcreteSoundAgent]
  private var clips: Map[Clips, AudioClip] = Map()
  private var medias: Map[Music, MediaPlayer] = Map()
  private var actualMusicPlayer: Option[MediaPlayer] = None
  private var actualClipPlayer: Option[AudioClip] = None
  private var volumeMusic: Double = DefaultVolumeMusic
  private var volumeEffect: Double = DefaultVolumeVolume
  cacheSounds()

  override def run(): Unit = {
    while (true) {
      Try(blockingQueue.take()) match {
        case Success(ev) => ev.handle(this)
        case Failure(exception) => logger warn exception.getMessage
      }
    }
  }

  override def playMusic(media: Music): Unit = {
    if (!medias.contains(media)) {
      medias += (media -> new MediaPlayer(new Media(loadFromJarToString(media.entryName))))
    }
    actualMusicPlayer = medias.get(media)
    actualMusicPlayer.foreach(_.setVolume(volumeMusic))
    actualMusicPlayer.foreach(_.play())
    actualMusicPlayer.foreach(_.setCycleCount(MediaPlayer.INDEFINITE))
    medias.filterNot(_._2 == actualMusicPlayer.get).foreach(_._2.pause())
  }

  override def stopMusic(): Unit = actualMusicPlayer.foreach(_.stop())

  override def pauseMusic(): Unit = actualMusicPlayer.foreach(_.pause())

  override def resumeMusic(): Unit = actualMusicPlayer.foreach(_.play())

  override def playClip(clip: Clips): Unit = {
    if (!clips.contains(clip)) {
      clips += (clip -> new AudioClip(loadFromJarToString(clip.entryName)))
    }
    actualClipPlayer = Some(clips(clip))
    actualClipPlayer.get.play(volumeEffect)
  }

  override def setVolumeMusic(value: Double): Unit = {
    volumeMusic = if (value < SoundErrorThresholds) 0.0 else value
    actualMusicPlayer.foreach(_.setVolume(volumeMusic))
  }

  override def setVolumeEffect(value: Double): Unit = {
    volumeEffect = if (value < SoundErrorThresholds) 0.0 else value
    actualClipPlayer.foreach(_.setVolume(volumeEffect))
  }

  override def restartMusic(): Unit = {
    actualMusicPlayer.foreach(_.stop())
    actualMusicPlayer.foreach(_.seek(Duration.ZERO))
    actualMusicPlayer.foreach(_.play())
  }

  override def enqueueEvent(ev: MediaEvent): Unit = blockingQueue.add(ev)

  private def cacheSounds(): Unit = {
    //Increase loading speed with cache
    medias += (Music.Home -> new MediaPlayer(new Media(loadFromJarToString(Music.Home.entryName))))
    medias += (Music.Game -> new MediaPlayer(new Media(loadFromJarToString(Music.Game.entryName))))
    medias(Music.Home).play()
    medias(Music.Home).stop()
    medias(Music.Game).play()
    medias(Music.Game).stop()
  }

  private final object MagicValues {
    val DefaultVolumeMusic = 0.5
    val DefaultVolumeVolume = 1.0
    val SoundErrorThresholds = 0.05
  }
}

/** Factory for [[SoundAgent]] instances. */
object SoundAgent {
  /** Creates a new [[SoundAgent]] */
  def apply(): SoundAgent = new ConcreteSoundAgent
}
