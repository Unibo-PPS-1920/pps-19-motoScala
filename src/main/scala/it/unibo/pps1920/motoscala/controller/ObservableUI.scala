package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
import it.unibo.pps1920.motoscala.view.ObserverUI

trait ObservableUI extends SoundController {
  type Level = Int
  def becomeHost(): Unit
  def tryJoinLobby(ip: String, port: String): Unit
  def kickSomeone(name: String): Unit
  def setSelfReady(): Unit
  def shutdownMultiplayer(): Unit
  def leaveLobby(): Unit
  def attachUI(obs: ObserverUI*): Unit
  def detachUI(obs: ObserverUI*): Unit
  def setupGame(level: Level): Unit
  def mediator: Mediator
  def loadAllLevels(): Unit
  def loadSetting(): Unit
  def loadStats(): Unit
  def saveStats(settings: SettingsData): Unit
  def start(): Unit
  def pause(): Unit
  def resume(): Unit
  def stop(): Unit
}


