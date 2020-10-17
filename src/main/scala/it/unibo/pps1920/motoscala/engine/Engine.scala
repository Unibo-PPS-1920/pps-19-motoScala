package it.unibo.pps1920.motoscala.engine


import java.util.UUID

import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.mediation.Commandable
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandData, CommandEvent, LevelSetupEvent}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.LevelSetupData
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
  def apply(controller: EngineController, myUuid: UUID): Engine = new GameEngineImpl(controller, myUuid)
  private class GameEngineImpl(controller: EngineController, myUuid: UUID) extends Engine {
    private val mediator = controller.mediator
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
      coordinator.registerComponentType(classOf[CollisionComponent])
      coordinator.registerComponentType(classOf[ShapeComponent])
      coordinator.registerComponentType(classOf[VelocityComponent])
      coordinator.registerComponentType(classOf[AIComponent])

      coordinator.registerSystem(DrawSystem(mediator, coordinator, myUuid))
      coordinator.registerSystem(AISystem(coordinator, eventQueue))
      coordinator.registerSystem(EndGameSystem(coordinator, mediator, Vector2(level.mapSize.x, level.mapSize.y), this))
      coordinator.registerSystem(CollisionsSystem(coordinator, controller, Fps))
      coordinator.registerSystem(MovementSystem(coordinator))
      coordinator.registerSystem(InputSystem(coordinator, eventQueue))

      val player = BumperCarEntity(myUuid)
      logger info "" + level.entities
      level.entities.foreach {

        case Player(position, shape, _, velocity) =>
          logger info "add player"
          coordinator.addEntity(player)
            .addEntityComponent(player, ShapeComponent(shape))
            .addEntityComponent(player, PositionComponent(util.Vector2(position.x, position.y)))
            .addEntityComponent(player, VelocityComponent(Vector2(0, 0), Vector2(velocity.x, velocity.y)))

            .addEntityComponent(player, CollisionComponent(4, isColliding = false, 0, (0, 0)))

        case BlackPupa(position, shape, _, velocity)
        =>
          logger info "add black pupa"
          val black = BlackPupaEntity(UUID.randomUUID())
          coordinator.addEntity(black)
            .addEntityComponent(black, ShapeComponent(shape))
            .addEntityComponent(black, PositionComponent(util.Vector2(position.x + 100, position.y + 100)))
            .addEntityComponent(black, VelocityComponent(Vector2(0, 0), util.Vector2(velocity.x, velocity.y)))
            .addEntityComponent(black, CollisionComponent(4, isColliding = false, 0, Vector2(0, 0)))
            .addEntityComponent(black, AIComponent(1, myUuid))
        case RedPupa(position, shape, _, velocity)
        =>
          logger info "add red pupa"
          val red = RedPupaEntity(UUID.randomUUID())
          coordinator.addEntity(red)
            .addEntityComponent(red, ShapeComponent(shape))
            .addEntityComponent(red, PositionComponent(util.Vector2(position.x + 100, position.y + 100)))
            .addEntityComponent(red, VelocityComponent(Vector2(0, 0), util.Vector2(velocity.x, velocity.y)))
            .addEntityComponent(red, CollisionComponent(4, isColliding = false, 0, Vector2(0, 0)))
            .addEntityComponent(red, AIComponent(4, myUuid))
        case BluePupa(position, shape, _, velocity)
        =>
          logger info "add blue pupa"
          val blue = BluePupaEntity(UUID.randomUUID())
          coordinator.addEntity(blue)
            .addEntityComponent(blue, ShapeComponent(shape))
            .addEntityComponent(blue, PositionComponent(util.Vector2(position.x + 100, position.y + 100)))
            .addEntityComponent(blue, VelocityComponent(Vector2(0, 0), util.Vector2(velocity.x, velocity.y)))
            .addEntityComponent(blue, CollisionComponent(4, isColliding = false, 0, Vector2(0, 0)))
        case Polar(position, shape, _, velocity)
        =>
          logger info "add polar"
          val polar = PolarEntity(UUID.randomUUID())
          coordinator.addEntity(polar)
            .addEntityComponent(polar, ShapeComponent(shape))
            .addEntityComponent(polar, PositionComponent(util.Vector2(position.x + 100, position.y + 100)))
            .addEntityComponent(polar, VelocityComponent(Vector2(0, 0), util.Vector2(velocity.x, velocity.y)))
            .addEntityComponent(polar, CollisionComponent(4, isColliding = false, 0, Vector2(0, 0)))
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

