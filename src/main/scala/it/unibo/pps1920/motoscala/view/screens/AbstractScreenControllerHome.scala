package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.UpdatableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.effect.{BoxBlur, DropShadow}
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.shape.Line

abstract class AbstractScreenControllerHome(protected override val viewFacade: ViewFacade,
                                            protected override val controller: UpdatableUI) extends ScreenController(viewFacade, controller) {
  import javafx.scene.layout.GridPane


  private val shadow = new DropShadow(10, Color.BLACK)
  private val blur = new BoxBlur(1, 1, 3)
  @FXML protected var title: ImageView = _
  @FXML protected var root: BorderPane = _
  @FXML protected var textPlay: Button = _
  @FXML protected var textSettings: Button = _
  @FXML protected var textStats: Button = _
  @FXML protected var textExit: Button = _
  @FXML protected var mainGridPane: GridPane = _

  @FXML override def initialize(): Unit = {
    import javafx.stage
    import scalafx.animation.{Timeline, TranslateTransition}
    import scalafx.util.Duration
    assertNodeInjected()
    val screenHeight = stage.Screen.getScreens.get(0).getBounds.getHeight
    val screenWidth = stage.Screen.getScreens.get(0).getBounds.getWidth
    val yPortions = screenHeight / 10
    val xPortions = screenWidth / 8

    -5 to 10 foreach (i => {

      val yLine = new Line(-10000, yPortions * i, 10000, yPortions * i)
      yLine.setStrokeWidth(3)
      val translate: TranslateTransition = new TranslateTransition
      translate.setDuration(Duration.apply(5000))
      translate.setToY(screenHeight + yPortions * i)
      translate.setByY(0)
      translate.setRate(0.5)
      translate.setCycleCount(Timeline.Indefinite)
      translate.setNode(yLine)
      translate.setAutoReverse(true)
      translate.play()
      this.root.getChildren.add(0, yLine)

    })

    1 to 8 foreach (i => {
      val xLine = new Line(xPortions * i, -10000, xPortions * i, 10000)
      xLine.setStrokeWidth(3)
      this.root.getChildren.add(0, xLine)
    })


    println("sss" +
              this.root.getWidth)
    println("ssss" +
              this.root.getHeight)


    println(stage.Screen.getScreens.get(0).getBounds.getHeight)
    println(stage.Screen.getScreens.get(0).getBounds.getWidth)


    mainGridPane.setEffect(shadow)

  }
  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Home.fxml'.")
  }


}
