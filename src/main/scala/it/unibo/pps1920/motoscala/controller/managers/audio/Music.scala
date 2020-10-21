package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._
import it.unibo.pps1920.motoscala.controller.managers.file.ResourcesJarPaths


/** A Music represents the path container for the file needed in one [[javafx.scene.media.MediaPlayer]].
 *
 * @constructor create a new Music.
 * @param entryName the path
 */
sealed abstract class Music(override val entryName: String) extends EnumEntry

object Music extends Enum[Music] {
  final override def values: IndexedSeq[Music] = findValues

  final case object Home extends Music(s"${ResourcesJarPaths.Music}Menu.mp3")
  final case object Game extends Music(s"${ResourcesJarPaths.Music}Game.mp3")
}

