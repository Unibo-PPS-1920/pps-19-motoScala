package it.unibo.pps1920.motoscala.view

import com.sun.javafx.application.PlatformImpl
import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.screens._
import it.unibo.pps1920.motoscala.view.utilities.ViewStateMachine
import it.unibo.pps1920.motoscala.view.utilities.ViewUtils.GlobalViewConstants.{SCREEN_MIN_HEIGHT, SCREEN_MIN_WIDTH}
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import scalafx.scene.layout.StackPane

private[view] trait ViewFacade {
  def changeScreen(screen: ScreenEvent): Unit
  def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit
}

trait View extends ObserverUI {
  def start(): Unit
}

object View {
  def apply(controller: ObservableUI): View = {
    require(controller != null)
    PlatformImpl.startup(() => {})
    new ViewImpl(controller)
  }

  private class ViewImpl(controller: ObservableUI) extends View with ViewFacade {
    private val logger = LoggerFactory getLogger classOf[View]
    private val stateMachine = ViewStateMachine.buildStateMachine()
    private val screenLoader = ScreenLoader()
    private val root = new StackPane()
    private val scene = new Scene(root, 200, 200)
    private var stage: Option[Stage] = None

    //Controller will observe me
    controller attachUI this
    //Loading all screens in cache
    loadScreens()

    override def start(): Unit = {
      Platform.runLater(() => {
        val stg = new Stage()
        stg.getIcons.add(new Image("/images/Icon.png"))
        stg setScene scene
        stg.show()
        stg setMinHeight SCREEN_MIN_WIDTH
        stg setMinWidth SCREEN_MIN_HEIGHT
        stg setOnCloseRequest (_ => System.exit(0))
        changeScreen(ScreenEvent.GotoHome)
        logger info s"View started on ${Thread.currentThread()}"
        stage = Some(stg)
      })
    }

    override def changeScreen(event: ScreenEvent): Unit = {
      val s = stateMachine.consume(event)
      screenLoader.applyScreen(s, root)
    }

    override def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit = screenLoader
      .loadFXMLNode(screen, controller)

    override def notify(ev: ViewEvent): Unit = ev match {
      case event: ViewEvent.HomeEvent => screenLoader.getScreenController(FXMLScreens.HOME).notify(event)
      case event: ViewEvent.GameEvent => screenLoader.getScreenController(FXMLScreens.GAME).notify(event)
      case event: ViewEvent.LobbyEvent => logger info event.getClass.toString
      case event: ViewEvent.SettingsEvent => logger info event.getClass.toString
      case event: ViewEvent.StatsEvent => logger info event.getClass.toString
    }

    private def loadScreens(): Unit = {
      loadFXMLNode(FXMLScreens.HOME, new ScreenControllerHome(this, controller))
      loadFXMLNode(FXMLScreens.GAME, new ScreenControllerGame(this, controller))
    }
  }
}


