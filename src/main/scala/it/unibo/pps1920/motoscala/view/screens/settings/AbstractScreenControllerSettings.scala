package it.unibo.pps1920.motoscala.view.screens.settings

import java.lang
import java.util.function.UnaryOperator

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.model.Difficulties.{EASY, HARD, MEDIUM}
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Slider, TextField, TextFormatter}
import javafx.scene.layout.{AnchorPane, BorderPane}
import javafx.util.StringConverter

/** Abstract ScreenController dedicated to show settings menu.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
abstract class AbstractScreenControllerSettings(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {

  @FXML protected var root: BorderPane = _
  @FXML protected var mainAnchorPane: AnchorPane = _
  @FXML protected var effectSlider: Slider = _
  @FXML protected var diffSlider: Slider = _
  @FXML protected var musicSlider: Slider = _
  @FXML protected var textPlayerName: TextField = _

  import MagicValues._
  @FXML override def initialize(): Unit = {
    assertNodeInjected(root, mainAnchorPane, effectSlider, diffSlider, musicSlider, textPlayerName)
    assertNodeInjected()
    extendButtonBackBehaviour()
    initBackButton()
    initSlider()
    initTextField()
  }

  private def initSlider(): Unit = {
    musicSlider.setOnMouseReleased(_ => sendStats())
    effectSlider.setOnMouseReleased(_ => sendStats())
    diffSlider.setLabelFormatter(new StringConverter[lang.Double]() {
      override def toString(`object`: lang.Double): String = `object` match {
        case n if n <= EASY.number => EASY.name
        case n if n <= MEDIUM.number => MEDIUM.name
        case n if n <= HARD.number => HARD.name
      }
      override def fromString(string: String): lang.Double = string match {
        case str if str == EASY.name => EASY.number
        case str if str == MEDIUM.name => MEDIUM.number
        case str if str == HARD.name => HARD.number
      }
    })
  }

  private def sendStats(): Unit =
    controller.saveSettings(SettingsData(musicSlider.getValue.toFloat, effectSlider.getValue.toFloat, diffSlider
      .getValue.toInt, if (textPlayerName.getText.isBlank) DefaultName else textPlayerName.getText))

  private def initTextField(): Unit = {
    val portFormatter: UnaryOperator[javafx.scene.control.TextFormatter.Change] = formatter => {
      val text: String = formatter.getControlNewText
      if (text.length <= NameMaxLength || text.isEmpty) formatter else null
    }
    textPlayerName.setTextFormatter(new TextFormatter(portFormatter))
  }

  private def extendButtonBackBehaviour(): Unit =
    buttonBack.addEventHandler[ActionEvent](ActionEvent.ACTION, _ => sendStats())

  def displaySettings(settings: SettingsData): Unit = {
    effectSlider.setValue(settings.effectVolume)
    musicSlider.setValue(settings.musicVolume)
    diffSlider.setValue(settings.diff)
    textPlayerName.setText(settings.name)
  }

  private[this] final object MagicValues {
    final val NameMaxLength = 6
    final val DefaultName = "Player"
  }
}
