package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.engine.GameStatus._
import org.slf4j.LoggerFactory
/**
 * Game Loop
 */
trait GameLoop extends Thread {
  /**
   * fps setter
   *
   * @param value new fps
   */
  def fps_=(value: Int): Unit
  /**
   * fps getter
   *
   * @return actual fps
   */
  def fps: Int
  /**
   * gets game status
   *
   * @return game status
   */
  def status: GameStatus
  /**
   * pauses the game
   *
   * @return new status
   */
  def pause(): GameStatus
  /**
   * resumes the game
   *
   * @return new status
   */
  def unPause(): GameStatus
  /**
   * stops the game
   *
   * @return new status
   */
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
      while (_status != Stopped) {
        if (_status == Paused) {
          this.synchronized {
            wait()
          }
        }
        val start = System.currentTimeMillis()

        engine.tick()

        val tickTime = System.currentTimeMillis() - start
        val deltaTime = getFrameMillis - tickTime
        if (deltaTime > 0) {
          Thread.sleep(deltaTime)
        } else logger warn s"Overrun by ${deltaTime} ms, please reduce FPS"
      }
    }

    private def getFrameMillis: Long = Ms / fps

    override def pause(): GameStatus = this.synchronized {
      _status match {
        case Running => _status = Paused; logger debug "pausing"; _status
        case Paused | Stopped => logger warn "Not running, can't pause"; _status
      }
    }

    override def unPause(): GameStatus = this.synchronized {
      _status match {
        case Paused => _status = Running; logger debug "resumed"; this.notifyAll(); _status
        case Running | Stopped => logger warn "Not paused, can't unpause"; _status
      }
    }

    override def halt(): GameStatus = this.synchronized {
      _status match {
        case Running | Paused => _status = Stopped; logger debug "stopped"; _status
        case Stopped => logger warn "Already stopped"; _status
      }
    }

    def status: GameStatus = this.synchronized {
      _status
    }
  }

}


