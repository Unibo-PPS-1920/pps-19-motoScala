package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.engine.GameStatus._

trait GameCycle extends Thread {
  def fps_=(value: Int): Unit
  def fps: Int = fps
  def status: GameStatus
  def pause: Unit
  def unPause: Unit
  def halt: Unit
}


class GameLoop private(
  override var fps: Int,
  val engine: UpdatableEngine
) extends GameCycle {

  import org.slf4j.LoggerFactory

  private val logger = LoggerFactory getLogger classOf[GameLoop]

  var _status: GameStatus = Stopped

  override def run(): Unit = {
    _status = Running
    while (_status == Running) {
      val start = System.currentTimeMillis()

      engine.tick()

      val tickTime = System.currentTimeMillis() - start
      val remainderTime = getFrameMillis - tickTime
      if (remainderTime > 0) {
        logger debug "wasting " + remainderTime + "ms"
        Thread.sleep(remainderTime)
      } else {
        logger debug "overrun by " + remainderTime + "ms"
      }
    }
  }

  def getFrameMillis: Long = 1000 / fps

  def pause: Unit = _status match {
    case Running => _status = Paused; logger debug "pausing"
    case Paused | Stopped => logger error "Not running, can't pause"
  }

  def unPause: Unit = _status match {
    case Paused => _status = Running; logger debug "resumed"
    case Running | Stopped => logger error "Not paused, can't unpause"
  }

  def halt: Unit = _status match {
    case Running | Paused => _status = Stopped; logger debug "stopped"
    case Stopped => logger error "Already stopped"
  }

  def status: GameStatus = _status
}

object GameLoop {
  def apply(fps: Int, engine: UpdatableEngine): GameCycle = new GameLoop(fps, engine)
}


