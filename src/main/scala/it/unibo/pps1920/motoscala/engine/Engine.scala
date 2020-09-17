package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.engine.GameStatus._

trait Engine extends UpdatableEngine {

}

class GameEngine private() extends Engine {
  private var gameLoop: Option[GameCycle] = None
  override def tick(): Unit = println("tick")
  def init: Unit = {
    gameLoop = Some(GameLoop(60, this))
  }
  def start: Unit = {
    gameLoop match {
      case Some(loop) =>
        loop.status match {
          case Stopped => loop.start()
          case _ => println("gameloop already running")
        }
      case None => println("gameloop absent")
    }

  }
  def pause(): Unit = gameLoop match {
    case Some(loop) => loop.pause
    case None => println("gameloop absent")
  }
  def resume(): Unit = gameLoop match {
    case Some(loop) => loop.unPause
    case None => println("gameloop absent")
  }
}

object GameEngine {
  def apply(): GameEngine = new GameEngine()
}

object Main extends App {
  val engine = GameEngine()
  engine.init
  engine.start
  Thread.sleep(5000)
  engine.pause()
  engine.resume()
}


