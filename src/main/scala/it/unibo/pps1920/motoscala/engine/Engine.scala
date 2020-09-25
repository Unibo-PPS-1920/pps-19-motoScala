package it.unibo.pps1920.motoscala.engine

import it.unibo.pps1920.motoscala.controller.LevelDescription
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandData, CommandEvent, CommandableEvent}
import it.unibo.pps1920.motoscala.controller.mediation.{Commandable, Mediator}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.engine.GameStatus._
import org.slf4j.LoggerFactory

import scala.collection.mutable

trait Engine extends UpdatableEngine with Commandable {
  def init(level: LevelDescription): Unit
  def start(): Unit
  def pause(): Unit
  def resume(): Unit
  def stop(): Unit
}


object GameEngine {


  def apply(mediator: Mediator): Engine = new GameEngineImpl(mediator)

  private class GameEngineImpl(mediator: Mediator) extends Engine {


    private val logger = LoggerFactory getLogger classOf[Engine]
    private val gameLoop = GameLoop(60, this)
    private val coordinator: Coordinator = Coordinator()
    private val eventQueue: mutable.Queue[CommandableEvent] = mutable.Queue()

    override def tick(): Unit = coordinator.updateSystems()

    override def init(level: LevelDescription): Unit = {
      import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
      import it.unibo.pps1920.motoscala.ecs.components.{PositionComponent, ShapeComponent}
      import it.unibo.pps1920.motoscala.ecs.util.Vector2
      import scalafx.scene.paint.Color
      val pos: PositionComponent = PositionComponent(Vector2(1, 2))
      var shape = ShapeComponent(Circle(3), Color(1, 1, 1, 1))

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

