package it.unibo.pps1920.motoscala.view.screens.home

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import it.unibo.pps1920.motoscala.view.utilities.ViewConstants
import javafx.fxml.FXML
import javafx.scene.CacheHint
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.layout.{AnchorPane, BorderPane, GridPane, Pane}
import javafx.scene.shape.Line
import scalafx.animation.{Timeline, TranslateTransition}
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration

/** Abstract ScreenController dedicated to show main menu.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
protected[home] abstract class AbstractScreenControllerHome(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {

  @FXML protected final var root: BorderPane = _
  @FXML protected final var mainAnchorPane: AnchorPane = _
  @FXML protected final var mainGridPane: GridPane = _
  @FXML protected final var title: ImageView = _
  @FXML protected final var textPlay: Button = _
  @FXML protected final var textPlayMultiplayer: Button = _
  @FXML protected final var textSettings: Button = _
  @FXML protected final var textStats: Button = _
  @FXML protected final var textExit: Button = _

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    initializeGrid(root)
    initializeBackground(root)
    initializeButtons()
  }

  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Home.fxml'.")
    assert(mainAnchorPane != null, "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'Home.fxml'.")
    assert(mainGridPane != null, "fx:id=\"mainGridPane\" was not injected: check your FXML file 'Home.fxml'.")
    assert(textPlay != null, "fx:id=\"textPlay\" was not injected: check your FXML file 'Home.fxml'.")
    assert(textPlayMultiplayer != null, "fx:id=\"textPlayMultiplayer\" was not injected: check your FXML file 'Home.fxml'.")
    assert(textSettings != null, "fx:id=\"textSettings\" was not injected: check your FXML file 'Home.fxml'.")
    assert(textStats != null, "fx:id=\"textStats\" was not injected: check your FXML file 'Home.fxml'.")
    assert(textExit != null, "fx:id=\"textExit\" was not injected: check your FXML file 'Home.fxml'.")
  }

  private def initializeButtons(): Unit = {
    def buttonClicked(screenEvent: ChangeScreenEvent): Unit = {
      controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonClick))
      viewFacade.changeScreen(screenEvent)
    }
    def buttonHovered(button: Button*): Unit =
      button.foreach(_.setOnMouseEntered(_ => controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonHover))))

    buttonHovered(textPlay, textPlayMultiplayer, textSettings, textStats, textExit)
    textPlay.setOnAction(_ => buttonClicked(ChangeScreenEvent.GotoLevels))
    textPlayMultiplayer.setOnAction(_ => buttonClicked(ChangeScreenEvent.GotoSelection))
    textSettings.setOnAction(_ => buttonClicked(ChangeScreenEvent.GotoSettings))
    textStats.setOnAction(_ => buttonClicked(ChangeScreenEvent.GotoStats))
    textExit.setOnAction(_ => System.exit(0))
  }
  private def initializeBackground(pane: Pane): Unit = {
    //Background setup
    val background: Rectangle = new Rectangle()
    background.width = ViewConstants.Window.ScreenWidth
    background.height = ViewConstants.Window.ScreenHeight
    background.setCache(true)
    background.setCacheHint(CacheHint.SPEED)
    background.setId(Constant.CSSBackgroundID)
    pane.getChildren.add(0, background)
  }
  private def initializeGrid(pane: Pane): Unit = {
    def setCache(node: Line): Unit = {
      node.setCache(true)
      node.setCacheHint(CacheHint.SPEED)
      node.setId(Constant.CSSLineID)
    }

    def addAnimation(node: Line, mul: Int): Unit = {
      val translate: TranslateTransition = new TranslateTransition
      translate.setDuration(Duration.apply(Constant.AnimationDuration * 2))
      translate.setToY((ViewConstants.Window.ScreenHeight + Constant.YLinePortion * mul) / 2)
      translate.setCycleCount(Timeline.Indefinite)
      translate.setNode(node)
      translate.setAutoReverse(true)
      translate.play()
    }
    //Vertical line setup
    Constant.LineNumberX.leftBound to Constant.LineNumberX.rightBound foreach (multiplier => {
      val xLine = new Line(0, Constant.XlinePortion * multiplier, Constant.PixelLineLength, Constant
        .XlinePortion * multiplier)
      setCache(xLine)
      addAnimation(xLine, multiplier)
      root.getChildren.add(0, xLine)
    })

    //Horizontal line setup
    Constant.LineNumberY.leftBound to Constant.LineNumberY.rightBound foreach (multiplier => {
      val yLine = new Line(Constant.YLinePortion * multiplier, -Constant.PixelLineLength, Constant
        .YLinePortion * multiplier, Constant.PixelLineLength)
      setCache(yLine)
      pane.getChildren.add(0, yLine)
    })
  }
  override def whenDisplayed(): Unit = {}
  private[this] final object Constant {
    final val AnimationDuration = 5000
    final val PixelLineLength = 5000
    final val ScreenLineDivider = 10
    final val XlinePortion = ViewConstants.Window.ScreenHeight / ScreenLineDivider
    final val YLinePortion = ViewConstants.Window.ScreenWidth / ScreenLineDivider
    final val CSSLineID = "Line"
    final val CSSBackgroundID = "Background"
    sealed trait LineNumber {
      def leftBound: Int;
      def rightBound: Int
    }
    case object LineNumberX extends LineNumber {
      val leftBound = -2
      val rightBound = 10
    }

    case object LineNumberY extends LineNumber {
      val leftBound = 1
      val rightBound = 10
    }

  }
}
