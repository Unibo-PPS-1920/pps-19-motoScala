package it.unibo.pps1920.motoscala.view

import com.sun.javafx.application.PlatformImpl
import it.unibo.pps1920.motoscala.controller.UpdatableUI
import it.unibo.pps1920.motoscala.view.FXMLScreens.FXMLScreens
import javafx.application.Platform
import javafx.scene.{Node, Scene}
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import scalafx.scene.layout.StackPane

trait ViewFacade extends ObserverUI {
  def start(): Unit
  def changeScreen(screen: FXMLScreens): Unit
  def loadFXMLNode(screen: FXMLScreens, controller: Object): Node
}

object ViewFacade {
  private class ViewFacadeImpl(controller: UpdatableUI) extends ViewFacade {
    private val logger = LoggerFactory getLogger classOf[ViewFacade]
    private val screenLoader = ScreenLoader()

    private var stage: Option[Stage] = None
    private val root = new StackPane()
    private val scene = new Scene(root, 200, 200)

    override def start(): Unit = {
      Platform.runLater(() => {
        stage = Some(new Stage())
        stage.get setScene scene
        stage.get.show()
        stage.get setOnCloseRequest (_ => System.exit(0))
        logger info s"View started on ${Thread.currentThread()}"
      })
    }
    override def changeScreen(screen: FXMLScreens): Unit = screenLoader.applyScreen(screen, root)
    override def loadFXMLNode(screen: FXMLScreens, controller: Object): Node = screenLoader
      .loadFXMLNode(screen, controller)
  }
  def apply(controller: UpdatableUI): ViewFacade = {
    require(controller != null)
    PlatformImpl.startup(() => {})
    new ViewFacadeImpl(controller)
  }
}


