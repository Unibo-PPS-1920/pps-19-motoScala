package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._
import it.unibo.pps1920.motoscala.controller.managers.file.ResourcesJarPaths


sealed abstract class Music(override val entryName: String) extends EnumEntry

object Music extends Enum[Music] {
  override def values: IndexedSeq[Music] = findValues

  case object Home extends Music(s"${ResourcesJarPaths.Music}vectorMenu.mp3")
  case object Game extends Music(s"${ResourcesJarPaths.Music}vector2.mp3")
}

