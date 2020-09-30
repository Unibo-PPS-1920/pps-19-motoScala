package it.unibo.pps1920.motoscala.view.screens.game

import java.util.UUID

import cats.syntax.option._
import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.LevelSetupData
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.model.Level.Coordinate
import it.unibo.pps1920.motoscala.view.screens.{ScreenController, ScreenEvent}
import it.unibo.pps1920.motoscala.view.{JavafxEnums, ViewFacade, iconSetter}
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.BorderPane
import org.kordamp.ikonli.material.Material

abstract class AbstractScreenControllerGame(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  private val gameEventHandler: GameEventHandler = new GameEventHandler()
  private var playerEntity: Option[Entity] = None
  private var mapSize: Option[Coordinate] = None

  @FXML protected var root: BorderPane = _
  @FXML protected var canvas: Canvas = _
  @FXML protected var buttonStart: Button = _
  @FXML protected var buttonBack: Button = _
  @FXML protected var labelTitle: Label = _

  private val PlayIcon = iconSetter(Material.PLAY_ARROW, JavafxEnums.MEDIUM_ICON)
  private val PauseIcon = iconSetter(Material.PAUSE, JavafxEnums.MEDIUM_ICON)

  def initialize(): Unit = {
    assertNodeInjected()
    initButtons()
    gameEventHandler.addKeyListeners(BumperCarEntity(UUID.randomUUID()))(root, sendCommandEvent)
  }
  private def dismiss(): Unit = {
    gameEventHandler.removeKeyListeners(root)
    controller.stop()
    viewFacade.changeScreen(ScreenEvent.GoBack)
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
    buttonStart.setGraphic(PauseIcon)
    buttonStart.setOnAction(_ => if (buttonStart.getGraphic == PlayIcon) {
      buttonStart.setGraphic(PauseIcon)
      controller.resume()
    } else {
      buttonStart.setGraphic(PlayIcon)
      controller.pause()
    })
  }
  protected def handleSetup(data: LevelSetupData): Unit = {
    playerEntity = data.playerEntity.some
    mapSize = data.level.mapSize.some
    if (data.isSinglePlayer || data.isHosting) {
      buttonStart setVisible true
      labelTitle.setText(if (data.isSinglePlayer) s"Level: ${data.level.index}" else "Multiplayer")
    } else {buttonStart setVisible false }
  }
  override def whenDisplayed(): Unit = {initialize(); root.requestFocus() }
  def sendCommandEvent(event: CommandEvent): Unit
}

