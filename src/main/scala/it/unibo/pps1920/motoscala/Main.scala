package it.unibo.pps1920.motoscala

import it.unibo.pps1920.motoscala.controller.Controller
import it.unibo.pps1920.motoscala.view.View

object Main extends App {
  val controller = Controller()
  val view = View(controller)
  view.start()
}
