package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._


/** A Clips represents the path container for the file needed in one [[javafx.scene.media.AudioClip]].
 *
 * @constructor create a new Clips
 * @param entryName the path
 */
sealed abstract class Clips(override val entryName: String) extends EnumEntry

object Clips extends Enum[Clips] {
  final override def values: IndexedSeq[Clips] = findValues

  final case object ButtonClick extends Clips("/clips/buttonClick.wav")
  final case object ButtonHover extends Clips("/clips/buttonHover.wav")
  final case object Collision extends Clips("/clips/collisionSoft.wav")
  final case object CollisionSoft extends Clips("/clips/collisionSoft.wav")
  final case object GameOver extends Clips("/clips/gameOver.wav")
  final case object PowerUpInvisible extends Clips("/clips/PowerUpInvisible.wav")
  final case object Invisible extends Clips("/clips/Invisible.wav")
  final case object PowerUpMass extends Clips("/clips/PowerUpMass.wav")
  final case object MoreMass extends Clips("/clips/moreMass.wav")
  final case object Heavy extends Clips("/clips/heavy.wav")
  final case object PowerUpSpeed extends Clips("/clips/PowerUpSpeed.wav")
  final case object Slower extends Clips("/clips/slower.wav")
  final case object Win extends Clips("/clips/YouWin.wav")
  final case object Out extends Clips("/clips/out.wav")
  final case object Swish extends Clips("/clips/generalFx.wav")
}
