package it.unibo.pps1920.motoscala.view.screens.game

import cats.syntax.option._
import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.{CommandEvent, EntityData}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.LevelSetupData
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity, Enemy1Entity, TileEntity}
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
  @FXML protected var buttonBack: Button = _
  @FXML protected var labelTitle: Label = _
  private var context: GraphicsContext = _

  private val PlayIcon = iconSetter(Material.PLAY_ARROW, JavafxEnums.MEDIUM_ICON)
  private val PauseIcon = iconSetter(Material.PAUSE, JavafxEnums.MEDIUM_ICON)

  def initialize(): Unit = {
    assertNodeInjected()
    context = canvas.getGraphicsContext2D
  }
  private def dismiss(): Unit = {
    gameEventHandler.foreach(_.dismiss())
    controller.stop()
    viewFacade.changeScreen(ScreenEvent.GoBack)
    viewFacade.getStage.setFullScreen(false)
  }
  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Game.fxml'.")
    assert(canvas != null, "fx:id=\"canvas\" was not injected: check your FXML file 'Game.fxml'.")
    assert(buttonStart != null, "fx:id=\"buttonStart\" was not injected: check your FXML file 'Game.fxml'.")
    assert(buttonBack != null, "fx:id=\"buttonBack\" was not injected: check your FXML file 'Game.fxml'.")
    assert(labelTitle != null, "fx:id=\"labelTitle\" was not injected: check your FXML file 'Game.fxml'.")
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
  protected def handleSetup(data: LevelSetupData): Unit = {
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
    initButtons()
    gameEventHandler = GameEventHandler(root, sendCommandEvent, playerEntity.get).some
    viewFacade.getStage.setFullScreen(true)
  }

  protected def drawEntities(player: EntityData, entities: Seq[EntityData]): Unit = {
    context.clearRect(0, 0, canvas.getWidth, canvas.getHeight)
    //context.drawImage(ImageLoader.getImage(Textures.BackgroundTexture), 0, 0, mapSize.get.x, mapSize.get.y)
    entities.foreach(e => e.entity match {
      case BumperCarEntity(_) =>
      case Enemy1Entity(_) =>
      case TileEntity(_) =>
    })
    Drawables.PlayerDrawable.draw(player)
  }

  override def whenDisplayed(): Unit = root.requestFocus()
  def sendCommandEvent(event: CommandEvent): Unit

  private object Drawables {
    val PlayerDrawable: EntityDrawable = new EntityDrawable(ImageLoader.getImage(Textures.ParticleTexture), context)
  }
}


