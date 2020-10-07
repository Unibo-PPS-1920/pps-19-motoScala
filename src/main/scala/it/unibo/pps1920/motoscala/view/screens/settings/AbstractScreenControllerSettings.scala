package it.unibo.pps1920.motoscala.view.screens.settings

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.{ScreenController, ScreenEvent}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Slider}
import javafx.scene.layout.{AnchorPane, BorderPane}
import javafx.util.StringConverter

abstract class AbstractScreenControllerSettings(protected override val viewFacade: ViewFacade,
                                                protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  @FXML protected var root: BorderPane = _
  @FXML protected var mainAnchorPane: AnchorPane = _
  @FXML protected var buttonBack: Button = _
  @FXML protected var diffSlider: Slider = _
  @FXML protected var volumeSlider: Slider = _

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    initBackButton()
    initSlider()
  }

  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Stats.fxml'.")
    assert(mainAnchorPane != null, "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'Stats.fxml'.")
    assert(diffSlider != null, "fx:id=\"diffSlider\" was not injected: check your FXML file 'Stats.fxml'.")
    assert(volumeSlider != null, "fx:id=\"volumeSlider\" was not injected: check your FXML file 'Stats.fxml'.")
  }

  private def initBackButton(): Unit = {
    buttonBack.setOnAction(_ => viewFacade.changeScreen(ScreenEvent.GoBack))
  }
  private def initSlider(): Unit = {


    diffSlider.setLabelFormatter(new StringConverter[Double] {

      override def toString(value: Double): String = {
        value match {
          case n if (n <= 1) => "Easy"
          case n if (n <= 2) => "Medium"
          case n if (n <= 3) => "Hard"
        }

      }
      override def fromString(string: String): Double = {
        string match {
          case "Easy" => 1.0d
          case "Medium" => 2.0d
          case "Hard" => 3.0d
        }
      }

    });

  }

}
