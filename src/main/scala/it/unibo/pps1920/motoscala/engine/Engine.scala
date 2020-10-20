package it.unibo.pps1920.motoscala.engine


import java.util.UUID

import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.mediation.Commandable
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandData, CommandEvent}
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.core.Coordinator
import it.unibo.pps1920.motoscala.ecs.entities._
import it.unibo.pps1920.motoscala.ecs.systems._
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.engine.GameStatus._
import it.unibo.pps1920.motoscala.model.Level._
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.util.Random

trait Engine extends UpdatableEngine with Commandable {
  def init(level: LevelData): Unit
  def start(): Unit
  def pause(): Unit
  def resume(): Unit
  def stop(): Unit
}

object GameEngine {
  def apply(controller: EngineController,
            players: List[BumperCarEntity]): Engine = new GameEngineImpl(controller, players)
  private class GameEngineImpl(controller: EngineController, players: List[BumperCarEntity]) extends Engine {
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
        .registerComponentType(classOf[ShapeComponent])
        .registerComponentType(classOf[VelocityComponent])
        .registerComponentType(classOf[AIComponent])
        .registerComponentType(classOf[JumpComponent])
        .registerComponentType(classOf[PowerUpComponent])
        .registerComponentType(classOf[ScoreComponent])

      coordinator.registerSystem(DrawSystem(mediator, coordinator, players.map(_.uuid)))
        .registerSystem(AISystem(coordinator, eventQueue, skipFrames = 10))
        .registerSystem(EndGameSystem(coordinator, mediator, Vector2(level.mapSize.x, level.mapSize.y), this))
        .registerSystem(CollisionsSystem(coordinator, controller, Fps))
        .registerSystem(MovementSystem(coordinator, Fps))
        .registerSystem(InputSystem(coordinator, eventQueue))
        .registerSystem(PowerUpSystem(coordinator, mediator))


      logger info "" + level.entities
      val iterablePlayers = players.iterator
      val playerStack = mutable.Stack[BumperCarEntity]()
      level.entities.foreach {

        case Player(position, shape, _, velocity) =>
          logger info "add player"
          val player = iterablePlayers.next()

          playerStack.push(player)
          coordinator.addEntity(player)
            .addEntityComponents(player,
                                 ShapeComponent(shape),
                                 PositionComponent((position.x, position.y)),
                                 VelocityComponent((0, 0), (velocity.x, velocity.y)),
                                 JumpComponent(),
                                 CollisionComponent(100, 20),
                                 ScoreComponent(0))
        case BlackPupa(position, shape, _, velocity) =>
          logger info "add black pupa"
          val black = BlackPupaEntity(UUID.randomUUID())
          coordinator.addEntity(black)
            .addEntityComponents(black, ShapeComponent(shape),
                                 PositionComponent((position.x + 100, position.y + 100)),
                                 VelocityComponent((0, 0), (velocity.x, velocity.y)),
                                 CollisionComponent(30, 10),
                                 AIComponent(10, Random.shuffle(playerStack)),
                                 ScoreComponent(300)
                                 )
        case RedPupa(position, shape, _, velocity) =>
          logger info "add red pupa"
          val red = RedPupaEntity(UUID.randomUUID())
          coordinator.addEntity(red)
            .addEntityComponents(red, ShapeComponent(shape),
                                 PositionComponent((position.x + 100, position.y + 100)),
                                 VelocityComponent((0, 0), (velocity.x, velocity.y)),
                                 CollisionComponent(30, 10),
                                 AIComponent(20, Random.shuffle(playerStack)),
                                 ScoreComponent(300)
                                 )
        case BluePupa(position, shape, _, velocity) =>
          logger info "add blue pupa"
          val blue = BluePupaEntity(UUID.randomUUID())
          coordinator.addEntity(blue)
            .addEntityComponents(blue, ShapeComponent(shape),
                                 PositionComponent((position.x + 100, position.y + 100)),
                                 VelocityComponent((0, 0), (velocity.x, velocity.y)),
                                 CollisionComponent(30, 10),
                                 AIComponent(40, Random.shuffle(playerStack)),
                                 ScoreComponent(300))
        case Polar(position, shape, _, velocity) =>
          logger info "add polar"
          val polar = PolarEntity(UUID.randomUUID())
          coordinator.addEntity(polar)
            .addEntityComponents(polar, ShapeComponent(shape),
                                 PositionComponent((position.x + 100, position.y + 100)),
                                 VelocityComponent((0, 0), (velocity.x, velocity.y)),
                                 CollisionComponent(30, 4),
                                 AIComponent(5, Random.shuffle(playerStack)),
                                 ScoreComponent(300)
                                 )
        case Nabicon(position, shape, _, velocity) =>
          import it.unibo.pps1920.motoscala.ecs.components.Shape.Rectangle
          logger info "add nabicon"
          val nabi = NabiconEntity(UUID.randomUUID())
          coordinator.addEntity(nabi)
            .addEntityComponents(nabi, ShapeComponent(Rectangle(100,50)),
                                 PositionComponent((position.x + 100, position.y + 100)),
                                 VelocityComponent((0, 0), (velocity.x, velocity.y)),
                                 CollisionComponent(20, Integer.MAX_VALUE),
                                 ScoreComponent(300))
        case Beecon(position, shape, _, velocity) =>
          logger info "add nabicon"
          val bee = BeeconEntity(UUID.randomUUID())
          coordinator.addEntity(bee)
            .addEntityComponents(bee, ShapeComponent(shape),
                                 PositionComponent((position.x + 100, position.y + 100)),
                                 VelocityComponent((0, 0), (velocity.x, velocity.y)),
                                 CollisionComponent(20, 400),
                                 ScoreComponent(1000)
                                 )
        case JumpPowerUp(position, shape) =>
          logger info "add jump powerUp"
          val jmp = JumpPowerUpEntity(UUID.randomUUID())
          coordinator.addEntity(jmp)
            .addEntityComponents(jmp, ShapeComponent(shape),
                                 PositionComponent((position.x, position.y)),
                                 CollisionComponent(10, 0),
                                 VelocityComponent((0, 0)),
                                 PowerUpComponent(effect = PowerUpEffect.JumpPowerUp(400)))
        case WeightBoostPowerUp(position, shape) =>
          logger info "add weight powerUp"
          val w = WeightPowerUpEntity(UUID.randomUUID())
          coordinator.addEntity(w)
            .addEntityComponents(w, ShapeComponent(shape),
                                 PositionComponent((position.x, position.y)),
                                 CollisionComponent(10, 0),
                                 VelocityComponent((0, 0)),
                                 PowerUpComponent(effect = PowerUpEffect.WeightBoostPowerUp(duration = 4, _ * 10)))
        case SpeedBoostPowerUp(position, shape) =>
          logger info "add weight powerUp"
          val s = SpeedPowerUpEntity(UUID.randomUUID())
          coordinator.addEntity(s)
            .addEntityComponents(s, ShapeComponent(shape),
                                 PositionComponent((position.x, position.y)),
                                 CollisionComponent(10, 0),
                                 VelocityComponent((0, 0)),
                                 PowerUpComponent(effect = PowerUpEffect.SpeedBoostPowerUp(duration = 20, _ dot 0.5)))
      }
      //mediator.publishEvent(LevelSetupEvent(LevelSetupData(level, isSinglePlayer = true, isHosting = true, playerStack.toList.head)))

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

