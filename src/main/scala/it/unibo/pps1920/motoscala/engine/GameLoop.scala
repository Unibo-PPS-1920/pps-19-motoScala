package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.engine.GameStatus._

trait GameCycle extends Thread {
  def fps_=(value: Int): Unit
  def fps: Int = fps
  def status: GameStatus
  def pause: Unit
  def unPause: Unit
}


class GameLoop private(
  override var fps: Int,
  val engine: UpdatableEngine
) extends GameCycle {

  var _status: GameStatus = Stopped

  override def run(): Unit = {
    _status = Running
    while (_status == Running) {
      val start = System.currentTimeMillis()

      engine.tick()

      val tickTime = System.currentTimeMillis() - start
      val frameTime = getFrameMillis

      if (tickTime < frameTime) {
        Thread.sleep(frameTime - tickTime)
      } else {

      }
    }
  }

  def getFrameMillis: Long = 1000 / fps

  def pause: Unit = _status match {
    case Running => _status = Paused; print("paused")
    case Paused | Stopped => print("Not running, cant pause")
  }

  def unPause: Unit = _status match {
    case Paused => _status = Running; print("resumed")
    case Running | Stopped => print("Not paused, cant unpause")
  }

  def status: GameStatus = _status
}

object GameLoop {
  def apply(fps: Int, engine: UpdatableEngine): GameCycle = new GameLoop(fps, engine)
}


