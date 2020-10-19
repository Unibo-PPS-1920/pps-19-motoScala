package it.unibo.pps1920.motoscala.view.screens.game

import cats.syntax.option._
import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.{PlayMusicEvent, PlaySoundEffect, StopMusic}
import it.unibo.pps1920.motoscala.controller.managers.audio.{Clips, Music}
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandEvent, EntityData}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.SetupData
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.entities._
import it.unibo.pps1920.motoscala.model.Level.Coordinate
import it.unibo.pps1920.motoscala.view.drawable.EntityDrawable
import it.unibo.pps1920.motoscala.view.loaders.ImageLoader
import it.unibo.pps1920.motoscala.view.screens.{ScreenController, ScreenEvent}
import it.unibo.pps1920.motoscala.view.utilities.ViewConstants.Entities.Textures
import it.unibo.pps1920.motoscala.view.{JavafxEnums, ViewFacade, iconSetter}
import javafx.fxml.FXML
import javafx.scene.canvas.{Canvas, GraphicsContext}
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{BorderPane, StackPane}
import org.kordamp.ikonli.material.Material

abstract class AbstractScreenControllerGame(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  private var gameEventHandler: Option[GameEventHandler] = None
  private var playerEntity: Option[Entity] = None
  private var mapSize: Option[Coordinate] = None
  private var isPlaying: Boolean = false

  @FXML protected var root: BorderPane = _
  @FXML protected var canvas: Canvas = _
  @FXML protected var canvasStack: StackPane = _
  @FXML protected var buttonStart: Button = _
  @FXML protected var labelTitle: Label = _
  @FXML protected var labelScore: Label = _
  private var context: GraphicsContext = _

  private val PlayIcon = iconSetter(Material.PLAY_ARROW, JavafxEnums.MEDIUM_ICON)
  private val PauseIcon = iconSetter(Material.PAUSE, JavafxEnums.MEDIUM_ICON)

  def initialize(): Unit = {
    assertNodeInjected()
    context = canvas.getGraphicsContext2D
  }
  private def dismiss(): Unit = {
    controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonClick))
    gameEventHandler.foreach(_.dismiss())
    controller.stop()
    viewFacade.changeScreen(ScreenEvent.GoBack)
    viewFacade.getStage.setFullScreen(false)
    controller.redirectSoundEvent(StopMusic())
    controller.redirectSoundEvent(PlayMusicEvent(Music.Home))
  }
  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Game.fxml'.")
    assert(canvas != null, "fx:id=\"canvas\" was not injected: check your FXML file 'Game.fxml'.")
    assert(buttonStart != null, "fx:id=\"buttonStart\" was not injected: check your FXML file 'Game.fxml'.")
    assert(buttonBack != null, "fx:id=\"buttonBack\" was not injected: check your FXML file 'Game.fxml'.")
    assert(labelTitle != null, "fx:id=\"labelTitle\" was not injected: check your FXML file 'Game.fxml'.")
    assert(labelScore != null, "fx:id=\"scoreTile\" was not injected: check your FXML file 'Game.fxml'.")
  }

  private def initButtons(): Unit = {
    //BACK
    buttonBack.setGraphic(iconSetter(Material.ARROW_BACK, JavafxEnums.MEDIUM_ICON))
    buttonBack.setOnAction(_ => dismiss())
    //PLAY-PAUSE
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
  protected def handleSetup(data: SetupData): Unit = {

    playerEntity = data.playerEntity.some
    mapSize = data.level.mapSize.some
    canvasStack.setMaxWidth(mapSize.get.x)
    canvasStack.setMaxHeight(mapSize.get.y)
    canvas.heightProperty().bind(canvasStack.maxHeightProperty())
    canvas.widthProperty().bind(canvasStack.maxWidthProperty())
    if (data.isSinglePlayer || data.isHosting) {
      buttonStart setVisible true
      labelTitle.setText(if (data.isSinglePlayer) s"Level: ${data.level.index}" else "Multiplayer")
    } else {buttonStart setVisible false }
    labelScore.setText(s"Score: ${0}")
    initButtons()
    gameEventHandler = GameEventHandler(root, sendCommandEvent, playerEntity.get).some
    viewFacade.getStage.setFullScreen(true)
  }
  protected def updateScore(points: Int): Unit = {
    labelScore.setText(s"Score: ${controller.updateScore(points)}")
  }

  protected def drawEntities(player: Option[EntityData], entities: Set[EntityData]): Unit = {
    context.clearRect(0, 0, canvas.getWidth, canvas.getHeight)
    entities.foreach(e => e.entity match {
      case BumperCarEntity(_) =>
      case RedPupaEntity(_) => Drawables.RedPupaDrawable.draw(e)
      case BlackPupaEntity(_) => Drawables.BlackPupaDrawable.draw(e)
      case BluePupaEntity(_) => Drawables.BluePupaDrawable.draw(e)
      case PolarEntity(_) => Drawables.PolarDrawable.draw(e)
      case PowerUpEntity(_) => Drawables.PowerUpDrawable.draw(e)
    })
    player.foreach(Drawables.PlayerDrawable.draw(_))
  }

  override def whenDisplayed(): Unit = {
    root.requestFocus()
    controller.redirectSoundEvent(PlayMusicEvent(Music.Game))
  }

  def sendCommandEvent(event: CommandEvent): Unit

  private object Drawables {
    val PlayerDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BumperCar), context)
    val BlackPupaDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BlackPupa), context)
    val BluePupaDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.BluePupa), context)
    val RedPupaDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.RedPupa), context)
    val PolarDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.Polar), context)
    val PowerUpDrawable: EntityDrawable = new EntityDrawable(ImageLoader
                                                               .getImage(Textures.ParticleTexture), context)
  }
}


