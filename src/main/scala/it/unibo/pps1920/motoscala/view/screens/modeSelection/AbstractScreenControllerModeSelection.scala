package it.unibo.pps1920.motoscala.view.screens.modeSelection

import java.util.function.UnaryOperator

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.model.NetworkAddr
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.screens.ScreenController
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

    def buttonHovered(button: Button*): Unit =
      button.foreach(_.setOnMouseEntered(_ => controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonHover))))

    buttonHovered(buttonHost, buttonJoin)

    buttonHost.setOnAction(_ => {
      controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonClick))
      controller.becomeHost()
      viewFacade.changeScreen(ChangeScreenEvent.GotoLobby)
    })

    buttonJoin.setOnAction(_ => {
      controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonClick))
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

    def getIpFormatter: UnaryOperator[Change] = getFormatter(_.matches(RegexIpAddress))

    def getPortFormatter: UnaryOperator[Change] =
      getFormatter(text => (text.length <= PortMaxLength && Try(NetworkAddr.validatePort(text.toInt))
        .getOrElse(false)) || text
        .isEmpty)


    ipTextField.setTextFormatter(new TextFormatter(getIpFormatter))

    portTextField.setTextFormatter(new TextFormatter(getPortFormatter))

    ipTextField.textProperty().addListener((_, _, newValue) => {
      if (NetworkAddr.validateIPV4Address(newValue)) {
        notInError(ipTextField)
        ipReady = true
      } else {
        inError(ipTextField)
        ipReady = false
      }
      checkIpAndPort()
    })

    portTextField.textProperty().addListener((_, _, newValue) => {
      if (Try(NetworkAddr.validatePort(newValue.toInt)).getOrElse(false)) {
        portReady = true
        notInError(portTextField)
      } else {
        inError(portTextField)
        portReady = false
      }
      checkIpAndPort()
    })

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
    final val RegexIpPartialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))"
    final val RegexIpSubsequentPartialBlock: String = "(\\." + RegexIpPartialBlock + ")"
    final val RegexIpAddress: String = "^" + (RegexIpPartialBlock + "?" + RegexIpSubsequentPartialBlock + "{0,3}")
    final val PortMaxLength = 5
    final val InErrorStyle = "-fx-border-color: red ; -fx-border-width: 5 ;"
    final val NotInErrorStyle = ""
  }
}

