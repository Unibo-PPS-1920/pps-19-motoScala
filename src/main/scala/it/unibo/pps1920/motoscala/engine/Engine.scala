package it.unibo.pps1920.motoscala.engine


import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.mediation.Commandable
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandData, CommandEvent}
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.core.Coordinator
import it.unibo.pps1920.motoscala.ecs.entities._
import it.unibo.pps1920.motoscala.ecs.systems._
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.engine.EngineUtils.addEntities
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
  def apply(controller: EngineController,
            players: List[BumperCarEntity],
            difficult: Int): Engine = new GameEngineImpl(controller, players, difficult)
  private class GameEngineImpl(controller: EngineController, players: List[BumperCarEntity],
                               difficult: Int) extends Engine {
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
        .registerSystem(PowerUpSystem(coordinator, mediator, Fps))
        .registerSystem(CollisionsSystem(coordinator, controller, Fps))
        .registerSystem(MovementSystem(coordinator, Fps))
        .registerSystem(InputSystem(coordinator, eventQueue))

      logger info "" + level.entities
      val iterablePlayers = players.iterator
      addEntities(coordinator, level, iterablePlayers, difficult)

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

