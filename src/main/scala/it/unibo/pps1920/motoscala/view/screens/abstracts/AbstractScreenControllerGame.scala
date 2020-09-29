package it.unibo.pps1920.motoscala.view.screens.abstracts

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import it.unibo.pps1920.motoscala.view.utilities.ViewConstants
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.CacheHint
import javafx.scene.input.{KeyCode, KeyEvent}
import javafx.scene.layout.{BorderPane, Pane}
import scalafx.scene.shape.Rectangle

abstract class AbstractScreenControllerGame(protected override val viewFacade: ViewFacade,
                                            protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  @FXML protected var root: BorderPane = _

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    this.initializeBackground(this.root)
    this.initializeButtons()
    this.root.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler[KeyEvent]() {
      override def handle(key: KeyEvent): Unit = {
        key.getCode match {
          case KeyCode.W => println("Chiamata")
          case KeyCode.S =>
          case KeyCode.A =>
          case KeyCode.D =>
        }
      }
    })
  }
  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Home.fxml'.")
  }

  private def initializeButtons(): Unit = {

  }

  private def initializeBackground(pane: Pane): Unit = {
    val background: Rectangle = new Rectangle()
    background.width = ViewConstants.SCREEN_WIDTH
    background.height = ViewConstants.SCREEN_HEIGHT
    background.setCache(true)
    background.setCacheHint(CacheHint.SPEED)
    background.setId(Constant.CSS_BACKGROUND_ID)
    pane.getChildren.add(0, background)
  }


  private[this] final object Constant {
    final val ANIMATION_DURATION = 5000
    final val PIXEL_LINE_LENGTH = 5000
    final val SCREEN_LINE_DIVIDER = 10
    final val X_LINE_NUMBER = (-2, 10)
    final val Y_LINE_NUMBER = (1, 10)
    final val X_LINE_PORTION = ViewConstants.SCREEN_HEIGHT / SCREEN_LINE_DIVIDER
    final val Y_LINE_PORTION = ViewConstants.SCREEN_WIDTH / SCREEN_LINE_DIVIDER
    final val CSS_LINE_ID = "Line"
    final val CSS_BACKGROUND_ID = "Background"

  }
}
