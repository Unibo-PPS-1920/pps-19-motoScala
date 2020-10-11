package it.unibo.pps1920.motoscala.view.screens.settings

import java.lang

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
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
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Settings.fxml'.")
    assert(mainAnchorPane != null, "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'Settings.fxml'.")
    assert(diffSlider != null, "fx:id=\"diffSlider\" was not injected: check your FXML file 'Settings.fxml'.")
    assert(volumeSlider != null, "fx:id=\"volumeSlider\" was not injected: check your FXML file 'Settings.fxml'.")
  }

  private def initBackButton(): Unit = {
    buttonBack.setOnAction(_ => {
      this.controller.saveStats(SettingsData(this.volumeSlider.getValue.toFloat, this.diffSlider.getValue.toInt))
      this.viewFacade.changeScreen(ScreenEvent.GoBack)
    })
  }
  private def initSlider(): Unit = {
    diffSlider.setLabelFormatter(new StringConverter[lang.Double]() {
      override def toString(`object`: lang.Double): String = `object` match {
        case n if (n <= 1.0d) => "Easy"
        case n if (n <= 2.0d) => "Medium"
        case n if (n <= 3.0d) => "Hard"
      }
      override def fromString(string: String): lang.Double = string match {
        case str if (str == "Easy") => 1.0d
        case str if (str == "Medium") => 2.0d
        case str if (str == "Hard") => 3.0d
      }
    });
  }

  def displaySettings(settings: SettingsData): Unit = {
    this.volumeSlider.setValue(settings.volume)
    this.diffSlider.setValue(settings.diff)
  }


}
