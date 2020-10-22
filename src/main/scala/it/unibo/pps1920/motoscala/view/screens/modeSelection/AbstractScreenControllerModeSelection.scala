package it.unibo.pps1920.motoscala.view.screens.modeSelection

import java.util.function.UnaryOperator

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.model.NetworkAddr
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField, TextFormatter}
import javafx.scene.layout.{AnchorPane, BorderPane}

import scala.util.Try

/** Abstract ScreenController dedicated to show the menu for selecting, client or server.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
abstract class AbstractScreenControllerModeSelection(
  protected override val viewFacade: ViewFacade,
  protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {

  @FXML protected var root: BorderPane = _
  @FXML protected var mainAnchorPane: AnchorPane = _
  @FXML protected var buttonHost: Button = _
  @FXML protected var buttonJoin: Button = _
  @FXML protected var ipTextField: TextField = _
  @FXML protected var portTextField: TextField = _
  private var ipReady: Boolean = false
  private var portReady: Boolean = false

  import MagicValues._

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    initButtons()
    initBackButton()
    initTextFields()
  }

  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'ModeSelection.fxml'.")
    assert(mainAnchorPane != null, "fx:id=\"mainAnchorPane\" was not injected: check your FXML file 'ModeSelection.fxml'.")
    assert(buttonBack != null, "fx:id=\"buttonBack\" was not injected: check your FXML file 'ModeSelection.fxml'.")
    assert(buttonHost != null, "fx:id=\"buttonHost\" was not injected: check your FXML file 'ModeSelection.fxml'.")
    assert(buttonJoin != null, "fx:id=\"buttonJoin\" was not injected: check your FXML file 'ModeSelection.fxml'.")
    assert(ipTextField != null, "fx:id=\"ipTextField\" was not injected: check your FXML file 'ModeSelection.fxml'.")
    assert(portTextField != null, "fx:id=\"portTextField\" was not injected: check your FXML file 'ModeSelection.fxml'.")
  }

  private def initButtons(): Unit = {

    def addButtonMusic(button: Button*): Unit = {
      button.foreach(_.addEventHandler[ActionEvent](ActionEvent.ACTION, _ => controller
        .redirectSoundEvent(PlaySoundEffect(Clips.ButtonClick))))
      button.foreach(_.setOnMouseEntered(_ => controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonHover))))
    }

    addButtonMusic(buttonHost, buttonJoin)

    buttonHost.setOnAction(_ => {
      controller.becomeHost()
      viewFacade.changeScreen(ChangeScreenEvent.GotoLobby)
    })

    buttonJoin.setOnAction(_ => {
      toggleButtons()
      controller.tryJoinLobby(ipTextField.getText, portTextField.getText())
    })
  }

  private def toggleButtons(): Unit = {
    buttonJoin.setDisable(!buttonJoin.isDisabled)
    buttonBack.setDisable(!buttonBack.isDisabled)
    buttonHost.setDisable(!buttonHost.isDisabled)
  }

  private def initTextFields(): Unit = {
    import javafx.scene.control.TextFormatter.Change

    def getFormatter(strategy: String => Boolean): UnaryOperator[Change] = formatter => {
      val value: String = formatter.getControlNewText
      if (strategy(value))
        formatter
      else
        null
    }

    def getIpFormatter: TextFormatter[Change] = new TextFormatter(getFormatter(_.matches(RegexIpAddress)))
    def getPortFormatter: TextFormatter[Change] =
      new TextFormatter(getFormatter(text => (text.length <= PortMaxLength && Try(NetworkAddr.validatePort(text.toInt))
        .getOrElse(false)) || text
        .isEmpty))

    ipTextField.setTextFormatter(getIpFormatter)
    portTextField.setTextFormatter(getPortFormatter)

    def setUpTextField(textField: TextField)(strategy: String => Boolean)(externalStrategy: Boolean => Unit): Unit = {
      textField.textProperty().addListener((_, _, newValue) => {
        if (strategy(newValue)) {
          notInError(ipTextField)
          externalStrategy(true)
        } else {
          externalStrategy(false)
          inError(ipTextField)
        }
        checkIpAndPort()
      })
    }

    setUpTextField(ipTextField)(NetworkAddr.validateIPV4Address)(ipReady = _)
    setUpTextField(portTextField)(newVal => Try(NetworkAddr.validatePort(newVal.toInt)).getOrElse(false))(portReady = _)

    def checkIpAndPort(): Unit = {
      if (ipReady && portReady)
        buttonJoin.setDisable(false)
      else
        buttonJoin.setDisable(true)
    }

    def inError(field: TextField): Unit = field.setStyle(InErrorStyle)
    def notInError(field: TextField): Unit = field.setStyle(NotInErrorStyle)
  }
  protected def displayResult(res: Boolean): Unit = {
    toggleButtons()
    if (res) viewFacade.changeScreen(ChangeScreenEvent.GotoLobby)
  }
  private[this] final object MagicValues {
    val RegexIpPartialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))"
    val PortMaxLength = 5
    val InErrorStyle = "-fx-border-color: red ; -fx-border-width: 5 ;"
    val NotInErrorStyle = ""
    val RegexIpSubsequentPartialBlock: String = "(\\." + RegexIpPartialBlock + ")"
    val RegexIpAddress: String = "^" + (RegexIpPartialBlock + "?" + RegexIpSubsequentPartialBlock + "{0,3}")
  }
}

