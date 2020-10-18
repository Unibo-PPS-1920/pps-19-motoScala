package it.unibo.pps1920.motoscala

import it.unibo.pps1920.motoscala.controller.Controller
import it.unibo.pps1920.motoscala.view.{View, initializeJavaFXThread}

object Main extends App {
  initializeJavaFXThread()

  val controller = Controller()
  val view = View(controller)
  view.start()

  //actorController.serverMode(GameEngine(controller.getMediator, UUID.randomUUID()))
}
