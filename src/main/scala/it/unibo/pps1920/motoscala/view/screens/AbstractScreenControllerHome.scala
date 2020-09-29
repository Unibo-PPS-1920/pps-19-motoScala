package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import javafx.fxml.FXML
import javafx.scene.CacheHint
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.shape.Line
import scalafx.animation.TranslateTransition
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration

abstract class AbstractScreenControllerHome(protected override val viewFacade: ViewFacade,
                                            protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  import javafx.scene.layout.{AnchorPane, GridPane, Pane}


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
    this.initializeGrid(this.root)
    this.initializeBackground(this.root)
    this.initializeButtons()
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
    this.textPlay.setOnAction(_ => {
      viewFacade.changeScreen(ScreenEvent.GotoGame)
    })

    this.textPlayMultiplayer.setOnAction(_ => {
      viewFacade.changeScreen(ScreenEvent.GotoLobby)
    })

    this.textSettings.setOnAction(_ => {
      viewFacade.changeScreen(ScreenEvent.GotoSettings)
    })

    this.textStats.setOnAction(_ => {
      viewFacade.changeScreen(ScreenEvent.GotoStats)
    })


    this.textExit.setOnAction(_ => {
      import javafx.application.Platform
      Platform.exit()
    })
  }

  private def initializeBackground(pane: Pane): Unit = {
    import it.unibo.pps1920.motoscala.view.utilities.ViewUtils.GlobalViewConstants
    //Background setup
    val background: Rectangle = new Rectangle()
    background.width = GlobalViewConstants.SCREEN_WIDTH
    background.height = GlobalViewConstants.SCREEN_HEIGHT
    background.setCache(true)
    background.setCacheHint(CacheHint.SPEED)
    background.setId(Constant.CSS_BACKGROUND_ID)
    pane.getChildren.add(0, background)
  }
  private def initializeGrid(pane: Pane): Unit = {
    import it.unibo.pps1920.motoscala.view.utilities.ViewUtils.GlobalViewConstants

    //Vertical line setup
    Constant.X_LINE_NUMBER._1 to Constant.X_LINE_NUMBER._2 foreach (multiplayer => {
      import scalafx.animation.Timeline
      val xLine = new Line(0, Constant.X_LINE_PORTION * multiplayer, Constant.PIXEL_LINE_LENGTH, Constant
        .X_LINE_PORTION * multiplayer)
      xLine.setCache(true)
      xLine.setCacheHint(CacheHint.SPEED)
      xLine.setId(Constant.CSS_LINE_ID)
      val translate: TranslateTransition = new TranslateTransition
      translate.setDuration(Duration.apply(Constant.ANIMATION_DURATION * 2))
      translate.setToY((GlobalViewConstants.SCREEN_HEIGHT + Constant.Y_LINE_PORTION * multiplayer) / 2)
      translate.setCycleCount(Timeline.Indefinite)
      translate.setNode(xLine)
      translate.setAutoReverse(true)
      translate.play()
      this.root.getChildren.add(0, xLine)
    })


    //Horizontal line setup
    Constant.Y_LINE_NUMBER._1 to Constant.Y_LINE_NUMBER._2 foreach (multiplayer => {
      val yLine = new Line(Constant.Y_LINE_PORTION * multiplayer, -Constant.PIXEL_LINE_LENGTH, Constant
        .Y_LINE_PORTION * multiplayer, Constant.PIXEL_LINE_LENGTH)
      yLine.setCache(true)
      yLine.setCacheHint(CacheHint.SPEED)
      yLine.setId(Constant.CSS_LINE_ID)
      pane.getChildren.add(0, yLine)
    })
  }

  private[this] final object Constant {
    import it.unibo.pps1920.motoscala.view.utilities.ViewUtils.GlobalViewConstants
    final val ANIMATION_DURATION = 5000
    final val PIXEL_LINE_LENGTH = 5000
    final val SCREEN_LINE_DIVIDER = 10
    final val X_LINE_NUMBER = (-2, 10)
    final val Y_LINE_NUMBER = (1, 10)
    final val X_LINE_PORTION = GlobalViewConstants.SCREEN_HEIGHT / SCREEN_LINE_DIVIDER
    final val Y_LINE_PORTION = GlobalViewConstants.SCREEN_WIDTH / SCREEN_LINE_DIVIDER
    final val CSS_LINE_ID = "Line"
    final val CSS_BACKGROUND_ID = "Background"

  }


}
