package it.unibo.pps1920.motoscala.view

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlayMusicEvent
import it.unibo.pps1920.motoscala.controller.managers.audio.Music
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.fsm.{ChangeScreenEvent, ViewStateMachine}
import it.unibo.pps1920.motoscala.view.screens._
import it.unibo.pps1920.motoscala.view.screens.game.ScreenControllerGame
import it.unibo.pps1920.motoscala.view.screens.home.ScreenControllerHome
import it.unibo.pps1920.motoscala.view.screens.levels.ScreenControllerLevels
import it.unibo.pps1920.motoscala.view.screens.lobby.ScreenControllerLobby
import it.unibo.pps1920.motoscala.view.screens.modeSelection.ScreenControllerModeSelection
import it.unibo.pps1920.motoscala.view.screens.settings.ScreenControllerSettings
import it.unibo.pps1920.motoscala.view.screens.stats.ScreenControllerStats
import it.unibo.pps1920.motoscala.view.utilities.ViewUtils
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import scalafx.scene.layout.StackPane

/** Facade class that exposes method to screens */
protected[view] trait ViewFacade {
  /** Get the primary stage */
  def getStage: Stage

  /** Display an other screen
   *
   * @param screen the Fxml screen
   */
  def changeScreen(screen: ChangeScreenEvent): Unit
}

/** Represents the view part in the mvc pattern */
trait View extends ObserverUI {
  /** Start the view and show the primary stage */
  def start(): Unit
}

object View {
  /** Factory for [[View]] instances */
  def apply(controller: ObservableUI): View = {
    require(controller != null)
    new ViewImpl(controller)
  }

  private class ViewImpl(controller: ObservableUI) extends View with ViewFacade {
    private val stateMachine = ViewStateMachine.buildStateMachine()
    private val screenLoader = ScreenLoader()
    private val root = new StackPane()
    private val scene = new Scene(root)
    private var stage: Option[Stage] = None

    controller attachUI this
    loadScreens()

    override def getStage: Stage = stage.get

    override def start(): Unit = {
      Platform.runLater(() => {
        val stage = ViewUtils.createStage(scene)
        changeScreen(ChangeScreenEvent.GotoHome)
        this.stage = Some(stage)
        controller.redirectSoundEvent(PlayMusicEvent(Music.Home))
      })
    }

    override def changeScreen(event: ChangeScreenEvent): Unit = {
      Platform.runLater(() => {
        screenLoader.applyScreen(stateMachine.consume(event), root)
        screenLoader.getScreenController(stateMachine.currentState).whenDisplayed()
      })
    }

    override def notify(ev: ViewEvent): Unit = ev match {
      case event: ViewEvent.HomeEvent => screenLoader.getScreenController(FXMLScreens.HOME).notify(event)
      case event: ViewEvent.GameEvent => screenLoader.getScreenController(FXMLScreens.GAME).notify(event)
      case event: ViewEvent.LevelEvent => screenLoader.getScreenController(FXMLScreens.LEVELS).notify(event)
      case event: ViewEvent.LobbyEvent => screenLoader.getScreenController(FXMLScreens.LOBBY).notify(event)
      case event: ViewEvent.SettingsEvent => screenLoader.getScreenController(FXMLScreens.SETTINGS).notify(event)
      case event: ViewEvent.StatsEvent => screenLoader.getScreenController(FXMLScreens.STATS).notify(event)
      case event: ViewEvent.SelectionEvent => screenLoader.getScreenController(FXMLScreens.SELECTION).notify(event)
      case _ =>
    }

    private def loadScreens(): Unit = {
      loadFXMLNode(FXMLScreens.STATS, new ScreenControllerStats(this, controller))
      loadFXMLNode(FXMLScreens.SETTINGS, new ScreenControllerSettings(this, controller))
      loadFXMLNode(FXMLScreens.LOBBY, new ScreenControllerLobby(this, controller))
      loadFXMLNode(FXMLScreens.GAME, new ScreenControllerGame(this, controller))
      loadFXMLNode(FXMLScreens.LEVELS, new ScreenControllerLevels(this, controller))
      loadFXMLNode(FXMLScreens.HOME, new ScreenControllerHome(this, controller))
      loadFXMLNode(FXMLScreens.SELECTION, new ScreenControllerModeSelection(this, controller))
    }

    private def loadFXMLNode(screen: FXMLScreens, controller: ScreenController): Unit = screenLoader
      .loadFXMLNode(screen, controller)
  }
}


