package it.unibo.pps1920.motoscala.view.screens.game

import cats.syntax.option._
import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.{PlayMusicEvent, PlaySoundEffect, StopMusic}
import it.unibo.pps1920.motoscala.controller.managers.audio.{Clips, Music}
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandEvent, EntityData}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.{EndData, LifeData}
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.entities._
import it.unibo.pps1920.motoscala.engine.Constants.PlayerLife
import it.unibo.pps1920.motoscala.model.Level.Coordinate
import it.unibo.pps1920.motoscala.view.events.ViewEvent.LevelSetupData
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import it.unibo.pps1920.motoscala.view.{JavafxEnums, ViewFacade, iconSetter, showDialog}
import javafx.fxml.FXML
import javafx.scene.canvas.{Canvas, GraphicsContext}
import javafx.scene.control.{Button, Label, ProgressBar}
import javafx.scene.layout.{BorderPane, StackPane}
import org.kordamp.ikonli.material.Material

abstract class AbstractScreenControllerGame(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  private val PlayIcon = iconSetter(Material.PLAY_ARROW, JavafxEnums.MediumIcon)
  private val PauseIcon = iconSetter(Material.PAUSE, JavafxEnums.MediumIcon)
  @FXML protected var root: BorderPane = _
  @FXML protected var canvas: Canvas = _
  @FXML protected var canvasStack: StackPane = _
  @FXML protected var buttonStart: Button = _
  @FXML protected var labelTitle: Label = _
  @FXML protected var labelScore: Label = _
  @FXML protected var lifeBar: ProgressBar = _
  private var context: GraphicsContext = _

  private var gameEventHandler: Option[GameEventHandler] = None
  private var playerEntity: Option[Entity] = None
  private var mapSize: Option[Coordinate] = None
  private var isPlaying: Boolean = false

  def initialize(): Unit = {
    assertNodeInjected()
    context = canvas.getGraphicsContext2D
  }

  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Game.fxml'.")
    assert(canvas != null, "fx:id=\"canvas\" was not injected: check your FXML file 'Game.fxml'.")
    assert(buttonStart != null, "fx:id=\"buttonStart\" was not injected: check your FXML file 'Game.fxml'.")
    assert(buttonBack != null, "fx:id=\"buttonBack\" was not injected: check your FXML file 'Game.fxml'.")
    assert(labelTitle != null, "fx:id=\"labelTitle\" was not injected: check your FXML file 'Game.fxml'.")
    assert(labelScore != null, "fx:id=\"scoreTile\" was not injected: check your FXML file 'Game.fxml'.")
    assert(lifeBar != null, "fx:id=\"lifeBar\" was not injected: check your FXML file 'Game.fxml'.")
  }

  private def initButtons(): Unit = {
    buttonBack.setGraphic(iconSetter(Material.ARROW_BACK, JavafxEnums.MediumIcon))
    buttonBack.setOnAction(_ => dismiss())
    isPlaying = false
    buttonStart.setGraphic(PlayIcon)
    buttonStart.setOnAction(_ => if (buttonStart.getGraphic == PlayIcon) {
      buttonStart.setGraphic(PauseIcon)
      if (!isPlaying) {controller.start(); isPlaying = true } else controller.resume()
    } else {
      buttonStart.setGraphic(PlayIcon)
      controller.pause()
    })
  }

  override def whenDisplayed(): Unit = {
    root.requestFocus()
    controller.redirectSoundEvent(PlayMusicEvent(Music.Game))
  }

  def sendCommandEvent(event: CommandEvent): Unit

  protected def handleLevelSetup(data: LevelSetupData): Unit = {
    playerEntity = data.player.some
    mapSize = data.level.mapSize.some
    canvasStack.setMaxWidth(mapSize.get.x)
    canvasStack.setMaxHeight(mapSize.get.y)
    canvasStack.setMinWidth(mapSize.get.x)
    canvasStack.setMinHeight(mapSize.get.y)
    canvas.heightProperty().bind(canvasStack.maxHeightProperty())
    canvas.widthProperty().bind(canvasStack.maxWidthProperty())
    lifeBar.setMinWidth(mapSize.get.x)
    lifeBar.setProgress(1.0)
    if (data.isSinglePlayer || data.isHosting) {
      buttonStart setVisible true
      labelTitle.setText(if (data.isSinglePlayer) s"Level: ${data.level.index}" else "Multiplayer")
    } else {buttonStart setVisible false }
    labelScore.setText(s"Score: ${0}")
    initButtons()
    gameEventHandler = GameEventHandler(root, sendCommandEvent, playerEntity.get).some
    viewFacade.getStage.setFullScreen(true)
  }

  private def dismiss(): Unit = {
    controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonClick))
    clearScreen()
    gameEventHandler.foreach(_.dismiss())
    controller.stop()
    viewFacade.changeScreen(ChangeScreenEvent.GoBack)
    viewFacade.getStage.setFullScreen(false)
    controller.redirectSoundEvent(StopMusic())
    controller.redirectSoundEvent(PlayMusicEvent(Music.Home))
    controller.shutdownMultiplayer()
  }

  private def clearScreen(): Unit = context.clearRect(0, 0, canvas.getWidth, canvas.getHeight)

  protected def handleLevelEnd(data: EndData): Unit = data match {
    case EndData(true, BumperCarEntity(_), _) =>
      showDialog(this.canvasStack, "You Win!",
                 controller.updateScore(gameIsEnded = true).toString, JavafxEnums.BigDialog, _ => dismiss())
    case EndData(false, BumperCarEntity(_), _) =>
      showDialog(this.canvasStack, "Game Over!",
                 controller.updateScore(gameIsEnded = true).toString, JavafxEnums.BigDialog, _ => dismiss())
    case EndData(_, _, score) => labelScore.setText(s"Score: ${controller.updateScore(Some(score))}")
  }

  protected def handleEntityLife(data: LifeData): Unit =
    if (data.entity == playerEntity.get) lifeBar.setProgress(data.life / PlayerLife.toDouble)

  protected def handleDrawEntities(players: Set[Option[EntityData]], entities: Set[EntityData]): Unit = {
    clearScreen()
    entities.foreach(e => e.entity match {
      case RedPupaEntity(_) => Drawables.RedPupaDrawable.draw(e, context)
      case BlackPupaEntity(_) => Drawables.BlackPupaDrawable.draw(e, context)
      case BluePupaEntity(_) => Drawables.BluePupaDrawable.draw(e, context)
      case PolarEntity(_) => Drawables.PolarDrawable.draw(e, context)
      case WeightPowerUpEntity(_) => Drawables.WeightDrawable.draw(e, context)
      case JumpPowerUpEntity(_) => Drawables.JumpDrawable.draw(e, context)
      case SpeedPowerUpEntity(_) => Drawables.SpeedDrawable.draw(e, context)
      case NabiconEntity(_) => Drawables.Block2Drawable.draw(e, context)
      case BeeconEntity(_) => Drawables.Block1Drawable.draw(e, context)
      case _ =>
    })
    val playerPart = players.partition(_.get.entity == playerEntity.get)
    playerPart._1.foreach(_ foreach (Drawables.PlayerDrawable.draw(_, context)))
    playerPart._2.foreach(_ foreach (Drawables.PlayerMPDrawable.draw(_, context)))
  }
}


