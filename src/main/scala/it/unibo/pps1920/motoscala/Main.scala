package it.unibo.pps1920.motoscala

import it.unibo.pps1920.motoscala.controller.Controller
import it.unibo.pps1920.motoscala.view.View

object Main extends App {
  import java.util.UUID
  import it.unibo.pps1920.motoscala.controller.mediation.Mediator
  import it.unibo.pps1920.motoscala.engine.GameEngine
  val controller = Controller()
  val view = View(controller)
  view.start()

  //actorController.serverMode(GameEngine(controller.getMediator, UUID.randomUUID()))
}
