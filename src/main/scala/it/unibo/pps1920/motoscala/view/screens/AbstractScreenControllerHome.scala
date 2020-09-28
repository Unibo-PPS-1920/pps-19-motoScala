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
    background.width = GlobalViewConstants.screenWidth
    background.height = GlobalViewConstants.screenHeight
    background.setCache(true)
    background.setCacheHint(CacheHint.SPEED)
    background.setId(Constant.cssBackgroundID)
    pane.getChildren.add(0, background)
  }
  private def initializeGrid(pane: Pane): Unit = {
    import it.unibo.pps1920.motoscala.view.utilities.ViewUtils.GlobalViewConstants

    //Vertical line setup
    Constant.xLineDividerBound._1 to Constant.xLineDividerBound._2 foreach (multiplayer => {
      import scalafx.animation.Timeline
      val xLine = new Line(0, Constant.xPortions * multiplayer, Constant.pixelLineBound, Constant
        .xPortions * multiplayer)
      xLine.setCache(true)
      xLine.setCacheHint(CacheHint.SPEED)
      xLine.setId(Constant.cssGridLineID)
      val translate: TranslateTransition = new TranslateTransition
      translate.setDuration(Duration.apply(Constant.animationLineDuration * 2))
      translate.setToY((GlobalViewConstants.screenHeight + Constant.yPortions * multiplayer) / 2)
      translate.setCycleCount(Timeline.Indefinite)
      translate.setNode(xLine)
      translate.setAutoReverse(true)
      translate.play()
      this.root.getChildren.add(0, xLine)
    })


    //Horizontal line setup
    Constant.yLineDividerBound._1 to Constant.yLineDividerBound._2 foreach (multiplayer => {
      val yLine = new Line(Constant.yPortions * multiplayer, -Constant.pixelLineBound, Constant
        .yPortions * multiplayer, Constant.pixelLineBound)
      yLine.setCache(true)
      yLine.setCacheHint(CacheHint.SPEED)
      yLine.setId(Constant.cssGridLineID)
      pane.getChildren.add(0, yLine)
    })
  }

  private[this] final object Constant {
    import it.unibo.pps1920.motoscala.view.utilities.ViewUtils.GlobalViewConstants
    final val animationLineDuration = 5000
    final val pixelLineBound = 5000
    final val screenLineDivider = 10
    final val xLineDividerBound = (-2, 10)
    final val yLineDividerBound = (1, 10)
    final val xPortions = GlobalViewConstants.screenHeight / screenLineDivider
    final val yPortions = GlobalViewConstants.screenWidth / screenLineDivider
    final val cssGridLineID = "Line"
    final val cssBackgroundID = "Background"

  }


}
