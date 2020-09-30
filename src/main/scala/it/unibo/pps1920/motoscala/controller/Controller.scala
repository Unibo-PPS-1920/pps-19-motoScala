package it.unibo.pps1920.motoscala.controller

import java.util.UUID
import java.util.UUID.randomUUID

import it.unibo.pps1920.motoscala
import it.unibo.pps1920.motoscala.controller.mediation.EntityType.Player
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.util
import it.unibo.pps1920.motoscala.engine.Engine
import it.unibo.pps1920.motoscala.model.Level.{LevelData, LevelEntity}
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LevelDataEvent
import it.unibo.pps1920.motoscala.view.{ObserverUI, View}
import org.slf4j.LoggerFactory

trait Controller extends ActorController with SoundController with ObservableUI {
}

object Controller {
  private class ControllerImpl private[Controller]() extends Controller {
    private val logger = LoggerFactory getLogger classOf[View]
    private var engine: Option[Engine] = None
    private var observers: Set[ObserverUI] = Set()
    private val mediator = Mediator()
    private var levels: List[LevelData] = List()
    private val myUuid: UUID = randomUUID()

    override def attachUI(obs: ObserverUI*): Unit = observers = observers ++ obs
    override def detachUI(obs: ObserverUI*): Unit = observers = observers -- obs
    override def setupGame(level: Level): Unit = {
      engine = Option(motoscala.engine.GameEngine(mediator, myUuid))
      engine.get.init(levels.filter(data => data.index == level).head)
    }

    override def start(): Unit = engine.get.start()
    override def getMediator: Mediator = mediator
    override def loadAllLevels(): Unit = {
      levels = List(LevelData(0, (100, 100), List(LevelEntity(Player, util.Vector2(50, 50)))))
      observers.foreach(o => o.notify(LevelDataEvent(levels)))
    }
    override def pause(): Unit = engine.get.resume()
    override def resume(): Unit = engine.get.resume()
    override def stop(): Unit = {
      engine.get.stop()
      engine = None
    }
  }
  def apply(): Controller = new ControllerImpl()
}


