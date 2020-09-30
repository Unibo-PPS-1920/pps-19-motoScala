package it.unibo.pps1920.motoscala.engine

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandData, CommandEvent, CommandableEvent, LevelSetupEvent}
import it.unibo.pps1920.motoscala.controller.mediation.{Commandable, EventData, Mediator}
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.systems.{DrawSystem, InputSystem, MovementSystem}
import it.unibo.pps1920.motoscala.engine.GameStatus._
import it.unibo.pps1920.motoscala.model.Level.{EnemyEntity, LevelData, PlayerEntity, TileEntity}
import org.slf4j.LoggerFactory

import scala.collection.mutable

trait Engine extends UpdatableEngine with Commandable {
  def init(level: LevelData): Unit
  def start(): Unit
  def pause(): Unit
  def resume(): Unit
  def stop(): Unit
}


object GameEngine {


  def apply(mediator: Mediator, myUuid: UUID): Engine = new GameEngineImpl(mediator, myUuid)

  private class GameEngineImpl(mediator: Mediator, myUuid: UUID) extends Engine {


    private val logger = LoggerFactory getLogger classOf[Engine]
    private val gameLoop = GameLoop(60, this)
    private val coordinator: Coordinator = Coordinator()
    private val eventQueue: mutable.Queue[CommandableEvent] = mutable.Queue()

    override def tick(): Unit = coordinator.updateSystems()

    override def init(level: LevelData): Unit = {
      mediator.subscribe(this)
      coordinator.registerComponentType(classOf[PositionComponent])
      coordinator.registerComponentType(classOf[VelocityComponent])
      coordinator.registerComponentType(classOf[DirectionComponent])
      coordinator.registerComponentType(classOf[IntangibleComponent])
      coordinator.registerComponentType(classOf[ShapeComponent])
      coordinator.registerComponentType(classOf[TypeComponent])
      coordinator.registerSystem(MovementSystem(coordinator))
      coordinator.registerSystem(DrawSystem(mediator, coordinator))
      coordinator.registerSystem(InputSystem(coordinator, eventQueue))
      val player = BumperCarEntity(myUuid)
      level.entities.foreach(e => e match {
        case TileEntity(shape, position, tangible) =>
        case PlayerEntity(position, shape, direction, velocity) =>
        case EnemyEntity(position, shape, direction, velocity) =>

      })
      mediator.publishEvent(LevelSetupEvent(EventData.LevelSetupData(isSinglePlayer = true, isHosting = true, player)))
    }
    override def start(): Unit = {
      gameLoop.status match {
        case Stopped => gameLoop.start()
        case _ => logger error "GameLoop already started"
      }
    }
    override def pause(): Unit = gameLoop.pause()

    override def resume(): Unit = gameLoop.unPause()

    override def stop(): Unit = {
      gameLoop.halt()
      mediator.unsubscribe(this)
    }

    override def notifyCommand(cmd: CommandData): Unit = {
      eventQueue.enqueue(CommandEvent(cmd))
      logger.info(cmd + "")
    }
  }

}

