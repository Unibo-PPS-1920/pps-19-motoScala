package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._

sealed abstract class Music(override val entryName: String) extends EnumEntry

object Music extends Enum[Music] {
  override def values: IndexedSeq[Music] = findValues
  case object Home extends Music("/music/home.mp3")
  case object Game extends Music("/music/game.mp3")
}

