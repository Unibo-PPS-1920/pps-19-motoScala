package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.view.ObserverUI

trait ObservableUI extends SoundController {
  type Level = Int
  def attachUI(obs: ObserverUI*): Unit
  def detachUI(obs: ObserverUI*): Unit
  def setupGame(level: Level): Unit
  def getMediator: Mediator
  def loadAllLevels(): Unit
  def start(): Unit
  def pause(): Unit
  def resume(): Unit
  def stop(): Unit
}


