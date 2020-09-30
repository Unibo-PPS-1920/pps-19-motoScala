package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.mediation.EntityType.Player
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.util
import it.unibo.pps1920.motoscala.engine.{Engine, GameEngine}
import it.unibo.pps1920.motoscala.model.Level.{LevelData, LevelEntity}
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.{ObserverUI, View}
import org.slf4j.LoggerFactory

trait Controller extends ActorController with SoundController with ObservableUI {
  def start(): Unit
}

object Controller {
  private class ControllerImpl private[Controller]() extends Controller {
    private val logger = LoggerFactory getLogger classOf[View]
    private var engine: Option[Engine] = None
    private var observers: Set[ObserverUI] = Set()
    private val mediator = Mediator()
    private var levels: List[LevelData] = List()

    override def attachUI(obs: ObserverUI*): Unit = observers = observers ++ obs
    override def detachUI(obs: ObserverUI*): Unit = observers = observers -- obs
    override def startGame(level: Level): Unit = {
      engine = Option(GameEngine(mediator))
      engine.get.init(levels.filter(data => data.index == level).head)
    }
    override def start(): Unit = logger info s"Controller started on ${Thread.currentThread()}"
    override def getMediator: Mediator = mediator
    override def loadAllLevels(): Unit = {
      levels = List(LevelData(1, (100, 100), List(LevelEntity(Player, util.Vector2(50, 50)))))
      mediator.publishEvent(ViewEvent)
    }
  }
  def apply(): Controller = new ControllerImpl()
}


