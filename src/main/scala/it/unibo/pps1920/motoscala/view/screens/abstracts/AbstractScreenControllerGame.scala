package it.unibo.pps1920.motoscala.view.screens.abstracts

import java.util.UUID

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.util.Direction._
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane

import scala.collection.mutable

abstract class AbstractScreenControllerGame(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  private val activeKeys: mutable.Map[KeyCode, Boolean] = mutable.Map()
  @FXML protected var root: BorderPane = _
  @FXML protected var canvas: Canvas = _

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    initKeyListener()
  }
  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Home.fxml'.")
  }

  override def whenDisplayed(): Unit = this.root.requestFocus()
  private def initKeyListener(): Unit = {
    def setKey(keyCode: KeyCode, direction: Direction, isActive: Boolean): Unit = {
      activeKeys += (keyCode -> isActive)
      logger info s"${if (isActive) "pressed" else "released"} $direction"
      sendCommandEvent(CommandEvent(CommandData(new Entity {
        override def uuid: UUID = UUID.randomUUID()
      }, direction, isActive)))
    }

    this.root.setOnKeyPressed(e => {
      val key = e.getCode
      if (!activeKeys.getOrElseUpdate(key, true)) {
        key match {
          case KeyCode.LEFT => setKey(key, West, isActive = true)
          case KeyCode.RIGHT => setKey(key, East, isActive = true)
          case KeyCode.UP => setKey(key, North, isActive = true)
          case KeyCode.DOWN => setKey(key, South, isActive = true)
          case _ => logger info ""
        }
      }
    })
    this.root.setOnKeyReleased(e => {
      val key = e.getCode
      if (activeKeys.getOrElseUpdate(key, false)) {
        key match {
          case KeyCode.LEFT => setKey(key, West, isActive = false)
          case KeyCode.RIGHT => setKey(key, East, isActive = false)
          case KeyCode.UP => setKey(key, North, isActive = false)
          case KeyCode.DOWN => setKey(key, South, isActive = false)
          case _ => logger info ""
        }
      }
    })
  }

  def sendCommandEvent(event: CommandEvent): Unit
}
