package it.unibo.pps1920.motoscala.controller.managers.audio

import enumeratum._
import it.unibo.pps1920.motoscala.controller.managers.file.ResourcesJarPaths

sealed abstract class Clips(override val entryName: String) extends EnumEntry


object Clips extends Enum[Clips] {
  override def values: IndexedSeq[Clips] = findValues
  case object Hit extends Clips(s"${ResourcesJarPaths.Clips}Hit.wav")
  case object ButtonClick extends Clips(s"${ResourcesJarPaths.Clips}buttonClick.wav")
  case object ButtonHover extends Clips(s"${ResourcesJarPaths.Clips}buttonHover.wav")
  case object Whip extends Clips(s"${ResourcesJarPaths.Clips}whip.wav")
  case object Collision extends Clips(s"${ResourcesJarPaths.Clips}collision.wav")
  case object Witch extends Clips(s"${ResourcesJarPaths.Clips}witch.wav")
  case object GameOver extends Clips(s"${ResourcesJarPaths.Clips}gameOver.wav")
  case object PowerUp1 extends Clips(s"${ResourcesJarPaths.Clips}powerUp1.wav")
  case object PowerUp2 extends Clips(s"${ResourcesJarPaths.Clips}powerUp2.wav")
  case object PowerUp3 extends Clips(s"${ResourcesJarPaths.Clips}powerUp3.wav")
  case object PowerUp4 extends Clips(s"${ResourcesJarPaths.Clips}powerUp4.wav")
  case object PowerUp5 extends Clips(s"${ResourcesJarPaths.Clips}powerUp5.wav")
  case object Levels extends Clips(s"${ResourcesJarPaths.Clips}levels.wav")
  case object Win extends Clips(s"${ResourcesJarPaths.Clips}win.wav")
  case object Out extends Clips(s"${ResourcesJarPaths.Clips}out.wav")
  case object Out2 extends Clips(s"${ResourcesJarPaths.Clips}out2.wav")
  case object Fx1 extends Clips(s"${ResourcesJarPaths.Clips}fx1.wav")
}
