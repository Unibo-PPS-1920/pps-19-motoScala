package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.engine.{Engine, GameEngine}
import it.unibo.pps1920.motoscala.view.ObserverUI
import org.slf4j.LoggerFactory

trait Controller extends ActorController with SoundController with UpdatableUI {
  def start(): Unit
}

object Controller {
  private class ControllerImpl private[Controller]() extends Controller {
    private val logger = LoggerFactory getLogger classOf[Controller]
    private var engine: Option[Engine] = None
    private var observers: Set[ObserverUI] = Set()

    override def attachUI(obs: ObserverUI*): Unit = observers = observers ++ obs
    override def detachUI(obs: ObserverUI*): Unit = observers = observers -- obs
    override def startGame(level: Level): Unit = engine = Option(GameEngine())
    override def start(): Unit = logger info "Controller started"
  }
  def apply(): Controller = new ControllerImpl()
}


