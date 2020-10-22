package it.unibo.pps1920.motoscala

import it.unibo.pps1920.motoscala.controller.Controller
import it.unibo.pps1920.motoscala.view.{View, initializeJavaFXThread}

/**
 * Main method that start all the application.
 * It starts the javafx toolkit.
 */
object Main extends App {
  initializeJavaFXThread()
  val controller = Controller()
  val view = View(controller)
  view.start()
}
