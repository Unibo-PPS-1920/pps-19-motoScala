package it.unibo.pps1920.motoscala.view.screens.home

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import it.unibo.pps1920.motoscala.view.utilities.ViewConstants
import it.unibo.pps1920.motoscala.view.utilities.ViewUtils.addButtonMusic
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.layout.{AnchorPane, BorderPane, GridPane, Pane}
import javafx.scene.shape.Line
import javafx.scene.{CacheHint, Node}
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
    assertNodeInjected(root, mainAnchorPane, mainGridPane, title, textPlay, textPlayMultiplayer, textSettings, textStats, textExit)
    initializeGrid(root)
    initializeBackground(root)
    initializeButtons()
  }


  private def initializeButtons(): Unit = {

    addButtonMusic(textPlay, textPlayMultiplayer, textSettings, textStats, textExit)(controller)

    textPlay.setOnAction(_ => viewFacade.changeScreen(ChangeScreenEvent.GotoLevels))
    textPlayMultiplayer.setOnAction(_ => viewFacade.changeScreen(ChangeScreenEvent.GotoSelection))
    textSettings.setOnAction(_ => viewFacade.changeScreen(ChangeScreenEvent.GotoSettings))
    textStats.setOnAction(_ => viewFacade.changeScreen(ChangeScreenEvent.GotoStats))
    textExit.setOnAction(_ => System.exit(0))
  }

  private def initializeBackground(pane: Pane): Unit = {
    //Background setup
    val background: Rectangle = new Rectangle()
    background.width = ViewConstants.Window.ScreenWidth
    background.height = ViewConstants.Window.ScreenHeight
    setCache(background)
    background.setId(MagicValues.CSSBackgroundID)
    pane.getChildren.add(0, background)
  }

  def setCache(node: Node): Unit = {
    node.setCache(true)
    node.setCacheHint(CacheHint.SPEED)
  }

  private def initializeGrid(pane: Pane): Unit = {

    def addAnimation(node: Line, mul: Int): Unit = {
      val translate: TranslateTransition = new TranslateTransition
      translate.setDuration(Duration.apply(MagicValues.AnimationDuration * 2))
      translate.setToY((ViewConstants.Window.ScreenHeight + MagicValues.YLinePortion * mul) / 2)
      translate.setCycleCount(Timeline.Indefinite)
      translate.setNode(node)
      translate.setAutoReverse(true)
      translate.play()
    }
    //Vertical line setup
    MagicValues.LineNumberX.leftBound to MagicValues.LineNumberX.rightBound foreach (multiplier => {
      val xLine = new Line(0, MagicValues.XlinePortion * multiplier, MagicValues.PixelLineLength, MagicValues
        .XlinePortion * multiplier)
      xLine.setId(MagicValues.CSSLineID)
      setCache(xLine)
      addAnimation(xLine, multiplier)
      root.getChildren.add(0, xLine)
    })

    //Horizontal line setup
    MagicValues.LineNumberY.leftBound to MagicValues.LineNumberY.rightBound foreach (multiplier => {
      val yLine = new Line(MagicValues.YLinePortion * multiplier, -MagicValues.PixelLineLength, MagicValues
        .YLinePortion * multiplier, MagicValues.PixelLineLength)
      yLine.setId(MagicValues.CSSLineID)
      setCache(yLine)
      pane.getChildren.add(0, yLine)
    })
  }
  override def whenDisplayed(): Unit = {}
  private[this] final object MagicValues {
    final val AnimationDuration = 5000
    final val PixelLineLength = 5000
    final val ScreenLineDivider = 10
    final val XlinePortion = ViewConstants.Window.ScreenHeight / ScreenLineDivider
    final val YLinePortion = ViewConstants.Window.ScreenWidth / ScreenLineDivider
    final val CSSLineID = "Line"
    final val CSSBackgroundID = "Background"
    sealed trait LineNumber {
      def leftBound: Int
      def rightBound: Int
    }
    case object LineNumberX extends LineNumber {
      val leftBound: Int = -2
      val rightBound: Int = 10
    }

    case object LineNumberY extends LineNumber {
      val leftBound: Int = 1
      val rightBound: Int = 10
    }

  }
}
