package it.unibo.pps1920.motoscala.view

import com.sun.javafx.application.PlatformImpl
import it.unibo.pps1920.motoscala.controller.UpdatableUI
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import scalafx.scene.layout.StackPane

trait ViewFacade extends ObserverUI {
  def start(): Unit
}

object ViewFacade {
  private class ViewFacadeImpl(controller: UpdatableUI) extends ViewFacade {
    private val logger = LoggerFactory getLogger classOf[ViewFacade]

    private var stage: Option[Stage] = None
    private val root = new StackPane()
    private val scene = new Scene(root, 200, 200)

    override def start(): Unit = {
      Platform.runLater(() => {
        stage = Some(new Stage())
        stage.get setScene scene
        stage.get.show()
        stage.get setOnCloseRequest (e => System.exit(0))
        logger info "View started"
      })
    }
  }
  def apply(controller: UpdatableUI): ViewFacade = {
    require(controller != null)
    PlatformImpl.startup(() => {})
    new ViewFacadeImpl(controller)
  }
}


