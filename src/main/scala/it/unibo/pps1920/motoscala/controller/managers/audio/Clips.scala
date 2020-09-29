package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._
sealed trait Clips extends EnumEntry

object Clips extends Enum[Clips] {
  override def values: IndexedSeq[Clips] = findValues
  case object Hit extends Clips
  case object Destroy extends Clips
}
