package it.unibo.pps1920.motoscala.controller

import java.util.UUID
import java.util.UUID.randomUUID

import it.unibo.pps1920.motoscala
import it.unibo.pps1920.motoscala.controller.managers.audio.{MediaEvent, SoundAgent}
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.components.Shape.Circle
import it.unibo.pps1920.motoscala.engine.Constants.MaxFps
import it.unibo.pps1920.motoscala.engine.Engine
import it.unibo.pps1920.motoscala.model.Level
import it.unibo.pps1920.motoscala.model.Level.{Coordinate, LevelData}
import it.unibo.pps1920.motoscala.view.ObserverUI
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LevelDataEvent
import it.unibo.pps1920.motoscala.view.utilities.ViewConstants
import org.slf4j.LoggerFactory

trait Controller extends ActorController with SoundController with EngineController with ObservableUI {
}

object Controller {
  def apply(): Controller = new ControllerImpl()
  private class ControllerImpl private[Controller](
    override val mediator: Mediator = Mediator()) extends Controller {
    private val logger = LoggerFactory getLogger classOf[ControllerImpl]
    private val myUuid: UUID = randomUUID()
    private var engine: Option[Engine] = None
    private var observers: Set[ObserverUI] = Set()
    private var levels: List[LevelData] = List()
    private val soundAgent: SoundAgent = SoundAgent()
    this.soundAgent.start()

    override def redirectSoundEvent(me: MediaEvent): Unit = this.soundAgent.enqueueEvent(me)
    override def attachUI(obs: ObserverUI*): Unit = observers = observers ++ obs
    override def detachUI(obs: ObserverUI*): Unit = observers = observers -- obs
    override def setupGame(level: Level): Unit = {
      logger info s"level selected: $level"
      engine = Option(motoscala.engine.GameEngine(this, myUuid))
      engine.get.init(levels.filter(data => data.index == level).head)
    }

    override def start(): Unit = engine.get.start()
    override def loadAllLevels(): Unit = {
      levels = List(LevelData(0, Coordinate(ViewConstants.Canvas.CanvasWidth, ViewConstants.Canvas.CanvasHeight),
                              List(Level.Player(Coordinate(500, 500), Circle(25), Coordinate(0, 0),
                                                Coordinate(10 * MaxFps, 10 * MaxFps)),
                                   Level.RedPupa(Coordinate(600, 500), Circle(25), Coordinate(0, 0),
                                                 Coordinate(5 * MaxFps, 5 * MaxFps)),
                                   Level.BlackPupa(Coordinate(600, 100), Circle(25), Coordinate(0, 0),
                                                   Coordinate(5 * MaxFps, 5 * MaxFps)),
                                   Level.Polar(Coordinate(600, 300), Circle(25), Coordinate(0, 0),
                                               Coordinate(5 * MaxFps, 5 * MaxFps)),
                                   Level.RedPupa(Coordinate(300, 100), Circle(25), Coordinate(0, 0),
                                                 Coordinate(5 * MaxFps, 5 * MaxFps)),

                                   Level.RedPupa(Coordinate(600, 200), Circle(25), Coordinate(0, 0),
                                                 Coordinate(5 * MaxFps, 5 * MaxFps)),
                                   Level
                                     .BlackPupa(Coordinate(700, 700), Circle(25), Coordinate(0, 0), Coordinate(5 * MaxFps, 5 * MaxFps))

                                   )))
      observers.foreach(o => o.notify(LevelDataEvent(levels)))
    }
    override def pause(): Unit = engine.get.pause()
    override def resume(): Unit = engine.get.resume()
    override def stop(): Unit = {
      engine.get.stop()
      engine = None
    }
  }
}


