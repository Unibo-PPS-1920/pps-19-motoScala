package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.engine.GameStatus._
import org.slf4j.LoggerFactory

trait Engine extends UpdatableEngine {}

class GameEngine private() extends Engine {


  private val logger = LoggerFactory getLogger classOf[Engine]
  private var gameLoop: Option[GameCycle] = None

  override def tick(): Unit = {Thread.sleep(14) }

  def init: Unit = {
    gameLoop = Some(GameLoop(60, this))
  }
  def start: Unit = {
    gameLoop match {
      case Some(loop) =>
        loop.status match {
          case Stopped => loop.start()
          case _ => logger error "GameLoop already started"
        }
      case None => logger error "Loop not initialized"
    }

  }
  def pause(): Unit = gameLoop match {
    case Some(loop) => loop.pause
    case None => logger error "Loop not initialized"
  }
  def resume(): Unit = gameLoop match {
    case Some(loop) => loop.unPause
    case None => logger error "Loop not initialized"
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
  //  engine.resume()
}


