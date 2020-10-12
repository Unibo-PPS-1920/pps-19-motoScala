package it.unibo.pps1920.motoscala.engine


import java.util.UUID

import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandData, CommandEvent, LevelSetupEvent}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.LevelSetupData
import it.unibo.pps1920.motoscala.controller.mediation.{Commandable, Mediator}
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.entities._
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator
import it.unibo.pps1920.motoscala.ecs.systems._
import it.unibo.pps1920.motoscala.ecs.util
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.engine.GameStatus._
import it.unibo.pps1920.motoscala.model.Level._
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

    private val Fps = 60
    private val logger = LoggerFactory getLogger classOf[Engine]
    private val gameLoop = GameLoop(Fps, this)
    private val coordinator: Coordinator = Coordinator()
    private val eventQueue: CommandQueue = CommandQueue()

    override def tick(): Unit = coordinator.updateSystems()

    override def init(level: LevelData): Unit = {
      logger info "engine init start"
      mediator.subscribe(this)
      coordinator.registerComponentType(classOf[PositionComponent])

      coordinator.registerComponentType(classOf[ShapeComponent])
      coordinator.registerComponentType(classOf[VelocityComponent])
      coordinator.registerComponentType(classOf[AIComponent])
      coordinator
        .registerSystem(EndGameSystem(coordinator, mediator, Vector2(level.mapSize.x, level.mapSize.y)))
      coordinator.registerSystem(MovementSystem(coordinator))
      coordinator.registerSystem(DrawSystem(mediator, coordinator, myUuid))
      coordinator.registerSystem(InputSystem(coordinator, eventQueue))
      coordinator.registerSystem(AISystem(coordinator, eventQueue))
      val player = BumperCarEntity(myUuid)
      logger info "" + level.entities
      level.entities.foreach {

        case Player(position, shape, direction, velocity) => {
          logger info "add player"
          coordinator.addEntity(player)
            .addEntityComponent(player, ShapeComponent(shape))
            .addEntityComponent(player, PositionComponent(util.Vector2(position.x, position.y)))
            .addEntityComponent(player, VelocityComponent(Vector2(0, 0), Vector2(velocity.x, velocity.y)))
          //.addEntityComponent(player, CollisionComponent(4, isColliding = false, 0, Center, Center, 0))
        }

        case BlackPupa(position, shape, direction, velocity) => {
          logger info "add black pupa"
          val black = BlackPupaEntity(UUID.randomUUID())
          coordinator.addEntity(black)
          coordinator.addEntityComponent(black, ShapeComponent(shape))
          coordinator.addEntityComponent(black, PositionComponent(util.Vector2(position.x, position.y)))
          coordinator.addEntityComponent(black, VelocityComponent(Vector2(0, 0), util.Vector2(velocity.x, velocity.y)))
        }
        case RedPupa(position, shape, direction, velocity) => {
          logger info "add red pupa"
          val red = RedPupaEntity(UUID.randomUUID())
          coordinator.addEntity(red)
          coordinator.addEntityComponent(red, ShapeComponent(shape))
          coordinator.addEntityComponent(red, PositionComponent(util.Vector2(position.x, position.y)))
          coordinator.addEntityComponent(red, VelocityComponent(Vector2(0, 0), util.Vector2(velocity.x, velocity.y)))
          coordinator.addEntityComponent(red, AIComponent(0, myUuid))
        }
        case BluePupa(position, shape, direction, velocity) => {
          logger info "add blue pupa"
          val blue = BluePupaEntity(UUID.randomUUID())
          coordinator.addEntity(blue)
          coordinator.addEntityComponent(blue, ShapeComponent(shape))
          coordinator.addEntityComponent(blue, PositionComponent(util.Vector2(position.x, position.y)))
          coordinator.addEntityComponent(blue, VelocityComponent(Vector2(0, 0), util.Vector2(velocity.x, velocity.y)))
        }
        case Polar(position, shape, direction, velocity) => {
          logger info "add polar"
          val polar = PolarEntity(UUID.randomUUID())
          coordinator.addEntity(polar)
          coordinator.addEntityComponent(polar, ShapeComponent(shape))
          coordinator.addEntityComponent(polar, PositionComponent(util.Vector2(position.x, position.y)))
          coordinator.addEntityComponent(polar, VelocityComponent(Vector2(0, 0), util.Vector2(velocity.x, velocity.y)))

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

