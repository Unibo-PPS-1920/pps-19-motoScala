package it.unibo.pps1920.motoscala.view

import com.sun.javafx.application.PlatformImpl
import it.unibo.pps1920.motoscala.controller.UpdatableUI
import javafx.application.Platform
import org.slf4j.LoggerFactory

trait ViewLike extends ObserverUI {
  def start(): Unit
}

class View private(controller: UpdatableUI) extends ViewLike {
  PlatformImpl.startup(() => {})
  private val logger = LoggerFactory getLogger classOf[View]
  override def start(): Unit = Platform.runLater(() => logger info "View started")
}


object View {
  def apply(controller: UpdatableUI): ViewLike = {require(controller != null); new View(controller) }
}


