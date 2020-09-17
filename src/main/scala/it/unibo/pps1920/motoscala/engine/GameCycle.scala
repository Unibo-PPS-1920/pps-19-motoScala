package it.unibo.pps1920.motoscala.engine

trait GameCycle {
  def fps_=(value: Int): Unit
  def fps: Int = fps
}

class GameLoop private(
  override var fps: Int,
  val engine: UpdatableEngine
) extends GameCycle {


}

object GameLoop {
  def apply(fps: Int, engine: UpdatableEngine): GameCycle = new GameLoop(fps, engine)
}


