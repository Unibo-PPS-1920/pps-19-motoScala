package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._

sealed abstract class Clips(override val entryName: String) extends EnumEntry


object Clips extends Enum[Clips] {
  override def values: IndexedSeq[Clips] = findValues
  case object Hit extends Clips("/clips/Hit.wav")
  case object ButtonClick extends Clips("/clips/buttonClick.wav")
  case object ButtonHover extends Clips("/clips/buttonHover.wav")
  case object Whip extends Clips("/clips/whip.wav")
  case object Collision extends Clips("/clips/collisionShort.wav")
  case object CollisionSoft extends Clips("/clips/collisionSoft.wav")
  case object Witch extends Clips("/clips/witch.wav")
  case object GameOver extends Clips("/clips/gameOver.wav")
  case object PowerUpInvisible extends Clips("/clips/PowerUpInvisible.wav")
  case object Invisible extends Clips("/clips/Invisible.wav")
  case object PowerUpMass extends Clips("/clips/PowerUpMass.wav")
  case object PowerUpSpeed extends Clips("/clips/PowerUpSpeed.wav")
  case object PowerUp4 extends Clips("/clips/powerUp4.wav")
  case object PowerUp5 extends Clips("/clips/powerUp5.wav")
  case object Levels extends Clips("/clips/levels.wav")
  case object Win extends Clips("/clips/win.wav")
  case object Out extends Clips("/clips/out.wav")
  case object Out2 extends Clips("/clips/out2.wav")
  case object Fx1 extends Clips("/clips/fx1.wav")
  case object Swish extends Clips("/clips/generalFx.wav")
}
