package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._
sealed trait Media extends EnumEntry

object Media extends Enum[Media] {
  override def values: IndexedSeq[Media] = findValues
  case object Menu extends Media
  case object Game extends Media
}

