package it.unibo.pps1920.motoscala

import it.unibo.pps1920.motoscala.controller.Controller
import it.unibo.pps1920.motoscala.view.ViewFacade

object Main extends App {
  val controller = Controller()
  val view = ViewFacade(controller)
  controller attachUI view
  controller.start()
  view.start()
}
