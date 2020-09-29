package it.unibo.pps1920.motoscala.view.screens.game

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.layout.BorderPane

abstract class AbstractScreenControllerGame(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {

  private val gameEventHandler: GameEventHandler = new GameEventHandler()

  @FXML protected var root: BorderPane = _
  @FXML protected var canvas: Canvas = _

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    gameEventHandler.addKeyListeners(root, sendCommandEvent)
  }
  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Home.fxml'.")
  }

  override def whenDisplayed(): Unit = this.root.requestFocus()

  def sendCommandEvent(event: CommandEvent): Unit
}