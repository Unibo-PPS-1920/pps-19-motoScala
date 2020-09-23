package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.engine.{Engine, GameEngine}
import it.unibo.pps1920.motoscala.view.{ObserverUI, View}
import org.slf4j.LoggerFactory

trait Controller extends ActorController with SoundController with UpdatableUI {
  def start(): Unit
}

object Controller {
  private class ControllerImpl private[Controller]() extends Controller {
    private val logger = LoggerFactory getLogger classOf[View]
    private var engine: Option[Engine] = None
    private var observers: Set[ObserverUI] = Set()
    private val mediator = Mediator()

    override def attachUI(obs: ObserverUI*): Unit = observers = observers ++ obs
    override def detachUI(obs: ObserverUI*): Unit = observers = observers -- obs
    override def startGame(level: Level): Unit = engine = Option(GameEngine(mediator))
    override def start(): Unit = logger info s"Controller started on ${Thread.currentThread()}"
  }
  def apply(): Controller = new ControllerImpl()
}


