package it.unibo.pps1920.motoscala.engine

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandData, CommandEvent, LevelSetupEvent}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.LevelSetupData
import it.unibo.pps1920.motoscala.controller.mediation.{Commandable, Mediator}
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity, Enemy1Entity, TileEntity}
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.systems.{DrawSystem, InputSystem, MovementSystem}
import it.unibo.pps1920.motoscala.ecs.util
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.engine.GameStatus._
import it.unibo.pps1920.motoscala.model.Level.{Enemy1, LevelData, Player, Tile}
import org.slf4j.LoggerFactory

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
    private val eventQueue: CommandQueue = CommandQueue()

    override def tick(): Unit = coordinator.updateSystems()

    override def init(level: LevelData): Unit = {
      logger info "engine init start"
      mediator.subscribe(this)
      coordinator.registerComponentType(classOf[PositionComponent])
      coordinator.registerComponentType(classOf[ShapeComponent])
      coordinator.registerComponentType(classOf[DirectionComponent])
      coordinator.registerComponentType(classOf[VelocityComponent])
      coordinator.registerComponentType(classOf[IntangibleComponent])
      coordinator.registerSystem(MovementSystem(coordinator))
      coordinator.registerSystem(DrawSystem(mediator, coordinator, myUuid))
      coordinator.registerSystem(InputSystem(coordinator, eventQueue))
      val player = BumperCarEntity(myUuid)
      logger info "" + level.entities
      level.entities.foreach {
        case Tile(shape, position, tangible) => {
          logger info "add tile"
          val tile = TileEntity(UUID.randomUUID())
          coordinator.addEntity(tile)
          coordinator.addEntityComponent(tile, ShapeComponent(shape))
          coordinator.addEntityComponent(tile, PositionComponent(util.Vector2(position.x, position.y)))
          if (!tangible) coordinator.addEntityComponent(tile, IntangibleComponent())
        }
        case Player(position, shape, direction, velocity) => {
          logger info "add player"
          coordinator.addEntity(player)
          coordinator.addEntityComponent(player, ShapeComponent(shape))
          coordinator.addEntityComponent(player, PositionComponent(util.Vector2(position.x, position.y)))
          coordinator
            .addEntityComponent(player, DirectionComponent(util.Direction(Vector2(direction.x, direction.y))))
          coordinator.addEntityComponent(player, VelocityComponent(velocity))
        }
        case Enemy1(position, shape, direction, velocity) => {
          logger info "add enemy"
          val enemy = Enemy1Entity(UUID.randomUUID())
          coordinator.addEntity(enemy)
          coordinator.addEntityComponent(enemy, ShapeComponent(shape))
          coordinator.addEntityComponent(enemy, PositionComponent(util.Vector2(position.x, position.y)))
          coordinator
            .addEntityComponent(enemy, DirectionComponent(util.Direction(Vector2(direction.x, direction.y))))
          coordinator.addEntityComponent(enemy, VelocityComponent(velocity))
        }
      }
      mediator.publishEvent(LevelSetupEvent(LevelSetupData(level, isSinglePlayer = true, isHosting = true, player)))
      logger info "engine init done"
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
    }
  }

}

