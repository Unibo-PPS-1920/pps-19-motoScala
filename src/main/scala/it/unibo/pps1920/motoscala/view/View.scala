package it.unibo.pps1920.motoscala.view

import com.sun.javafx.application.PlatformImpl
import it.unibo.pps1920.motoscala.controller.UpdatableUI
import it.unibo.pps1920.motoscala.view.screens.{FXMLScreens, ScreenControllerHome, ScreenEvent}
import it.unibo.pps1920.motoscala.view.utilities.ViewStateMachine
import javafx.application.Platform
import javafx.scene.{Node, Scene}
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import scalafx.scene.layout.StackPane

trait View extends ObserverUI {
  def start(): Unit
}

private[view] trait ViewFacade {
  def changeScreen(screen: ScreenEvent): Unit
  def loadFXMLNode(screen: FXMLScreens, controller: Object): Node
}

object View {
  private class ViewImpl(controller: UpdatableUI) extends View with ViewFacade {
    private val logger = LoggerFactory getLogger classOf[View]
    private val stateMachine = ViewStateMachine.buildStateMachine()
    private val screenLoader = ScreenLoader()

    private var stage: Option[Stage] = None
    private val root = new StackPane()
    private val scene = new Scene(root, 200, 200)

    override def start(): Unit = {
      Platform.runLater(() => {
        stage = Some(new Stage())
        stage.get setScene scene
        new ScreenControllerHome(this)
        stage.get.show()
        stage.get setOnCloseRequest (_ => System.exit(0))
        logger info s"View started on ${Thread.currentThread()}"
      })
    }
    override def changeScreen(event: ScreenEvent): Unit = screenLoader.applyScreen(stateMachine.consume(event), root)
    override def loadFXMLNode(screen: FXMLScreens, controller: Object): Node = screenLoader
      .loadFXMLNode(screen, controller)
  }
  def apply(controller: UpdatableUI): View = {
    require(controller != null)
    PlatformImpl.startup(() => {})
    new ViewImpl(controller)
  }
}


