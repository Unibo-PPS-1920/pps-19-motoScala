package it.unibo.pps1920.motoscala.view

import com.sun.javafx.application.PlatformImpl
import it.unibo.pps1920.motoscala.controller.UpdatableUI
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.screens.{FXMLScreens, ScreenController, ScreenControllerHome, ScreenEvent}
import it.unibo.pps1920.motoscala.view.utilities.ViewStateMachine
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import scalafx.scene.layout.StackPane

trait View extends ObserverUI {
  def start(): Unit
}

private[view] trait ViewFacade {
  def changeScreen(screen: ScreenEvent): Unit
  def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit
}

object View {
  private class ViewImpl(controller: UpdatableUI) extends View with ViewFacade {
    private val logger = LoggerFactory getLogger classOf[View]
    private val stateMachine = ViewStateMachine.buildStateMachine()
    private val screenLoader = ScreenLoader()

    private var stage: Option[Stage] = None
    private val root = new StackPane()
    private val scene = new Scene(root, 200, 200)

    controller attachUI this
    loadFXMLNode(FXMLScreens.HOME, new ScreenControllerHome(this, controller))

    override def start(): Unit = {
      Platform.runLater(() => {
        stage = Some(new Stage())
        stage.get setScene scene
        stage.get.show()
        changeScreen(ScreenEvent.GotoHome)
        stage.get setOnCloseRequest (_ => System.exit(0))
        logger info s"View started on ${Thread.currentThread()}"
      })
    }
    override def changeScreen(event: ScreenEvent): Unit = screenLoader.applyScreen(stateMachine.consume(event), root)
    override def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit = screenLoader
      .loadFXMLNode(screen, controller)
    override def notify(ev: ViewEvent): Unit = ev match {
      case event: ViewEvent.HomeEvent => screenLoader.getScreenController(FXMLScreens.HOME).notify(event)
      case event: ViewEvent.GameEvent => logger info event.getClass.toString
      case event: ViewEvent.LobbyEvent => logger info event.getClass.toString
      case event: ViewEvent.SettingsEvent => logger info event.getClass.toString
      case event: ViewEvent.StatsEvent => logger info event.getClass.toString
    }
  }
  def apply(controller: UpdatableUI): View = {
    require(controller != null)
    PlatformImpl.startup(() => {})
    new ViewImpl(controller)
  }
}


