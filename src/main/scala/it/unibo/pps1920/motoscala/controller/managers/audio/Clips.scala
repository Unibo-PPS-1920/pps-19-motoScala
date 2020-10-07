package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._

sealed abstract class Clips(override val entryName: String) extends EnumEntry


object Clips extends Enum[Clips] {
  override def values: IndexedSeq[Clips] = findValues
  case object Hit extends Clips("/clips/Hit.wav")
  case object Destroy extends Clips("")
}
