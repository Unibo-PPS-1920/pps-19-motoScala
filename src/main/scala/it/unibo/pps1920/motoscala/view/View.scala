package it.unibo.pps1920.motoscala.view

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlayMusicEvent
import it.unibo.pps1920.motoscala.controller.managers.audio.Music
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.screens._
import it.unibo.pps1920.motoscala.view.screens.`end`.ScreenControllerEndGame
import it.unibo.pps1920.motoscala.view.screens.game.ScreenControllerGame
import it.unibo.pps1920.motoscala.view.screens.home.ScreenControllerHome
import it.unibo.pps1920.motoscala.view.screens.levels.ScreenControllerLevels
import it.unibo.pps1920.motoscala.view.screens.lobby.ScreenControllerLobby
import it.unibo.pps1920.motoscala.view.screens.modeSelection.ScreenControllerModeSelection
import it.unibo.pps1920.motoscala.view.screens.settings.ScreenControllerSettings
import it.unibo.pps1920.motoscala.view.screens.stats.ScreenControllerStats
import it.unibo.pps1920.motoscala.view.utilities.{ViewStateMachine, ViewUtils}
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import scalafx.scene.layout.StackPane

private[view] trait ViewFacade {
  def getStage: Stage
  def changeScreen(screen: ScreenEvent): Unit
  def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit
}

trait View extends ObserverUI {
  def start(): Unit
}

object View {
  def apply(controller: ObservableUI): View = {
    require(controller != null)
    new ViewImpl(controller)
  }

  private class ViewImpl(controller: ObservableUI) extends View with ViewFacade {
    private val logger = LoggerFactory getLogger classOf[View]
    private val stateMachine = ViewStateMachine.buildStateMachine()
    private val screenLoader = ScreenLoader()
    private val root = new StackPane()
    private val scene = new Scene(root)
    private var stage: Option[Stage] = None

    controller attachUI this //Controller will observe me
    loadScreens() //Loading all screens in cache

    override def start(): Unit = {
      Platform.runLater(() => {
        val stage = ViewUtils.createStage(scene)
        changeScreen(ScreenEvent.GotoHome)
        this.stage = Some(stage)
        logger info s"View started on ${Thread.currentThread()}"
        controller.redirectSoundEvent(PlayMusicEvent(Music.Home))
      })
    }

    override def changeScreen(event: ScreenEvent): Unit = {
      Platform.runLater(() => {
        screenLoader.applyScreen(stateMachine.consume(event), root)
        screenLoader.getScreenController(stateMachine.currentState).whenDisplayed()
      })
    }

    override def getStage: Stage = stage.get

    override def notify(ev: ViewEvent): Unit = ev match {
      case event: ViewEvent.HomeEvent => screenLoader.getScreenController(FXMLScreens.HOME).notify(event)
      case event: ViewEvent.GameEvent => screenLoader.getScreenController(FXMLScreens.GAME).notify(event)
      case event: ViewEvent.LevelEvent => screenLoader.getScreenController(FXMLScreens.LEVELS).notify(event)
      case event: ViewEvent.LobbyEvent => screenLoader.getScreenController(FXMLScreens.LOBBY).notify(event)
      case event: ViewEvent.SettingsEvent => screenLoader.getScreenController(FXMLScreens.SETTINGS).notify(event)
      case event: ViewEvent.StatsEvent => screenLoader.getScreenController(FXMLScreens.STATS).notify(event)
      case event: ViewEvent.SelectionEvent => screenLoader.getScreenController(FXMLScreens.SELECTION).notify(event)
      case ViewEvent.ShowDialogEvent(title, msg, duration, notificationType) => Platform
        .runLater(() => showNotificationPopup(title, msg, duration, notificationType, _ => {}))
      case _ => logger warn s"Strange message ${ev}"
    }

    private def loadScreens(): Unit = {
      loadFXMLNode(FXMLScreens.STATS, new ScreenControllerStats(this, controller))
      loadFXMLNode(FXMLScreens.SETTINGS, new ScreenControllerSettings(this, controller))
      loadFXMLNode(FXMLScreens.LOBBY, new ScreenControllerLobby(this, controller))
      loadFXMLNode(FXMLScreens.GAME, new ScreenControllerGame(this, controller))
      loadFXMLNode(FXMLScreens.LEVELS, new ScreenControllerLevels(this, controller))
      loadFXMLNode(FXMLScreens.HOME, new ScreenControllerHome(this, controller))
      loadFXMLNode(FXMLScreens.SELECTION, new ScreenControllerModeSelection(this, controller))
      loadFXMLNode(FXMLScreens.END, new ScreenControllerEndGame(this, controller))
    }

    override def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit = screenLoader
      .loadFXMLNode(screen, controller)
  }
}


