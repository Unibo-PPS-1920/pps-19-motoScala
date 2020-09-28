package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.effect.DropShadow
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.stage
import scalafx.animation.{Timeline, TranslateTransition}
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration

abstract class AbstractScreenControllerHome(protected override val viewFacade: ViewFacade,
                                            protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  import javafx.scene.layout.{AnchorPane, GridPane}

  private final val lineDuration = 5000
  private final val pixelLineBound = 10000
  private final val screenLineDivider = 10
  private final val shadowBox = 50
  private final val animationRate = 0.5
  private final val lineStroke = 3
  private final val xLineDivederBound = (-5, 10)
  private final val yLineDivederBound = (1, 10)
  private final val lineColor = Color.WHITE
  private final val backgroundColor = Color.rgb(12, 45, 74)


  private val shadow = new DropShadow(shadowBox, Color.BLACK)
  @FXML protected var root: BorderPane = _
  @FXML protected var mainAnchorPane: AnchorPane = _
  @FXML protected var mainGridPane: GridPane = _
  @FXML protected var title: ImageView = _
  @FXML protected var textPlay: Button = _
  @FXML protected var textPlayMultiplayer: Button = _
  @FXML protected var textSettings: Button = _
  @FXML protected var textStats: Button = _
  @FXML protected var textExit: Button = _
  @FXML override def initialize(): Unit = {
    assertNodeInjected()

    val screenHeight = stage.Screen.getScreens.get(0).getBounds.getHeight
    val screenWidth = stage.Screen.getScreens.get(0).getBounds.getWidth
    val yPortions = screenHeight / screenLineDivider
    val xPortions = screenWidth / screenLineDivider

    this.mainGridPane.setEffect(shadow)

    //Vertical line setup
    xLineDivederBound._1 to xLineDivederBound._2 foreach (divider => {
      val translate: TranslateTransition = new TranslateTransition
      val yLine = new Line(-pixelLineBound, yPortions * divider, pixelLineBound, yPortions * divider)
      yLine.setStroke(lineColor)
      yLine.setStrokeWidth(lineStroke)
      translate.setDuration(Duration.apply(lineDuration))
      translate.setToY(screenHeight + yPortions * divider)
      translate.setByY(0)
      translate.setRate(animationRate)
      translate.setCycleCount(Timeline.Indefinite)
      translate.setNode(yLine)
      translate.setAutoReverse(true)
      translate.play()
      this.root.getChildren.add(0, yLine)
    })

    //Horizontal line setup
    yLineDivederBound._1 to yLineDivederBound._2 foreach (divider => {
      val xLine = new Line(xPortions * divider, -pixelLineBound, xPortions * divider, pixelLineBound)
      xLine.setStroke(lineColor)
      xLine.setStrokeWidth(lineStroke)
      this.root.getChildren.add(0, xLine)
    })

    //Background setup
    val background: Rectangle = new Rectangle()
    background.width = screenWidth
    background.height = screenHeight
    background.setFill(backgroundColor)
    this.root.getChildren.add(0, background)

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


}
