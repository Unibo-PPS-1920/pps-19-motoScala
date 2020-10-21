package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._

sealed abstract class Clips(override val entryName: String) extends EnumEntry
object Clips extends Enum[Clips] {
  override def values: IndexedSeq[Clips] = findValues
  case object ButtonClick extends Clips("/clips/buttonClick.wav")
  case object ButtonHover extends Clips("/clips/buttonHover.wav")
  case object Collision extends Clips("/clips/collisionSoft.wav")
  case object CollisionSoft extends Clips("/clips/collisionSoft.wav")
  case object GameOver extends Clips("/clips/gameOver.wav")
  case object PowerUpInvisible extends Clips("/clips/PowerUpInvisible.wav")
  case object Invisible extends Clips("/clips/Invisible.wav")
  case object PowerUpMass extends Clips("/clips/PowerUpMass.wav")
  case object MoreMass extends Clips("/clips/moreMass.wav")
  case object Heavy extends Clips("/clips/heavy.wav")
  case object PowerUpSpeed extends Clips("/clips/PowerUpSpeed.wav")
  case object Slower extends Clips("/clips/slower.wav")
  case object Win extends Clips("/clips/YouWin.wav")
  case object Out extends Clips("/clips/out.wav")
  case object Swish extends Clips("/clips/generalFx.wav")
}
