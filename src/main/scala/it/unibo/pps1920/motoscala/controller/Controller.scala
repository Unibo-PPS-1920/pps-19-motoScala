package it.unibo.pps1920.motoscala.controller

import it.unibo.pps1920.motoscala.controller.mediation.{Mediator, MediatorLike}
import it.unibo.pps1920.motoscala.engine.{Engine, GameEngine}
import it.unibo.pps1920.motoscala.view.{ObserverUI, View}
import org.slf4j.LoggerFactory

trait ControllerLike extends ActorController with SoundController with UpdatableUI {
  def start(): Unit
}

class Controller private() extends ControllerLike {
  private val logger = LoggerFactory getLogger classOf[View]
  private var engine: Option[Engine] = None
  private var observers: Set[ObserverUI] = Set()
  private val mediator: MediatorLike = Mediator()

  override def attachUI(obs: ObserverUI*): Unit = observers = observers ++ obs
  override def detachUI(obs: ObserverUI*): Unit = observers = observers -- obs
  override def startGame(level: Level): Unit = engine = Option(GameEngine())
  override def start(): Unit = logger info "Controller started"
}

object Controller {
  def apply(): Controller = new Controller()
}


