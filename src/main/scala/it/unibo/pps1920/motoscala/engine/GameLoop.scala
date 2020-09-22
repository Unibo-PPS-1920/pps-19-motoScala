package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.engine.GameStatus._
import org.slf4j.LoggerFactory

trait GameLoop extends Thread {
  def fps_=(value: Int): Unit
  def fps: Int
  def status: GameStatus
  def pause(): GameStatus
  def unPause(): GameStatus
  def halt(): GameStatus
}


private[engine] object GameLoop {
  def apply(fps: Int, engine: UpdatableEngine): GameLoop = new GameLoopImpl(fps, engine)

  private class GameLoopImpl(
    override var fps: Int,
    val engine: UpdatableEngine
  ) extends GameLoop {


    private val Ms = 1000
    private val logger = LoggerFactory getLogger classOf[GameLoop]

    @volatile private var _status: GameStatus = Stopped

    override def run(): Unit = {
      _status = Running
      //noinspection LoopVariableNotUpdated
      while (_status == Running) {
        val start = System.currentTimeMillis()

        engine.tick()

        val tickTime = System.currentTimeMillis() - start
        val deltaTime = getFrameMillis - tickTime
        if (deltaTime > 0) {
          //          logger debug "wasting " + deltaTime + "ms"
          Thread.sleep(deltaTime)
        } else {
          //          logger debug "overrun by " + deltaTime + "ms"
        }
      }
    }

    private def getFrameMillis: Long = Ms / fps

    override def pause(): GameStatus = this.synchronized {
      _status match {
        case Running => _status = Paused; logger debug "pausing"; _status
        case Paused | Stopped => logger error "Not running, can't pause"; _status
      }
    }

    override def unPause(): GameStatus = this.synchronized {
      _status match {
        case Paused => _status = Running; logger debug "resumed"; _status
        case Running | Stopped => logger error "Not paused, can't unpause"; _status
      }
    }

    override def halt(): GameStatus = this.synchronized {
      _status match {
        case Running | Paused => _status = Stopped; logger debug "stopped"; _status
        case Stopped => logger error "Already stopped"; _status
      }
    }

    def status: GameStatus = this.synchronized {
      _status
    }
  }

}


