package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.controller.mediation.Commandable
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandData, CommandEvent, CommandableEvent}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.engine.GameStatus._
import org.slf4j.LoggerFactory

import scala.collection.mutable

trait Engine extends UpdatableEngine with Commandable {
  def init(): Unit
  def start(): Unit
  def pause(): Unit
  def resume(): Unit
  def stop(): Unit
}


object GameEngine {
  def apply(): Engine = new GameEngineImpl()

  private class GameEngineImpl extends Engine {


    private val logger = LoggerFactory getLogger classOf[Engine]
    private val gameLoop = GameLoop(60, this)
    private val coordinator: Coordinator = Coordinator()
    private var eventQueue: mutable.Queue[CommandableEvent] = mutable.Queue()

    override def tick(): Unit = coordinator.updateSystems()

    override def init(): Unit = {

    }
    override def start(): Unit = {
      gameLoop.status match {
        case Stopped => gameLoop.start()
        case _ => logger error "GameLoop already started"
      }
    }
    override def pause(): Unit = gameLoop.pause()

    override def resume(): Unit = gameLoop.unPause()

    override def stop(): Unit = gameLoop.halt()

    override def notifyCommand(cmd: CommandData): Unit = eventQueue.enqueue(CommandEvent(cmd))
  }

}

