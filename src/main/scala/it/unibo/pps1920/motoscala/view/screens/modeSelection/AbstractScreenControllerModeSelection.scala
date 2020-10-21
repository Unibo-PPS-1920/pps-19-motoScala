package it.unibo.pps1920.motoscala.view.screens.modeSelection

import java.util.function.UnaryOperator

import it.unibo.pps1920.motoscala.controller.ObservableUI
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


  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    initButtons()
    initBackButton()
    initTextField()
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

  private def initTextField(): Unit = {
    val partialBlock: String = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))"
    val subsequentPartialBlock: String = "(\\." + partialBlock + ")"
    val ipAddressRegex: String = "^" + (partialBlock + "?" + subsequentPartialBlock + "{0,3}")

    val ipAddressFilter: UnaryOperator[javafx.scene.control.TextFormatter.Change] = formatter => {
      val value: String = formatter.getControlNewText
      if (value.matches(ipAddressRegex)) {
        formatter
      } else {
        null
      }
    }
    ipTextField.setTextFormatter(new TextFormatter(ipAddressFilter))


    val portFormatter: UnaryOperator[javafx.scene.control.TextFormatter.Change] = formatter => {
      val text: String = formatter.getControlNewText

      if ((text.length <= 5 && Try(NetworkAddr.validatePort(text.toInt)).getOrElse(false)) || text.isEmpty) {
        formatter
      } else {
        null
      }

    }
    portTextField.setTextFormatter(new TextFormatter(portFormatter))

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
      if (ipReady && portReady) {
        buttonJoin.setDisable(false)
      } else {
        buttonJoin.setDisable(true)
      }
    }

    def inError(field: TextField): Unit = {
      field
        .setStyle("-fx-border-color: red ; -fx-border-width: 5 ;")
    }

    def notInError(field: TextField): Unit = {
      field.setStyle("")
    }

  }

  protected def displayResult(res: Boolean): Unit = {
    toggleButtons()
    if (res) {
      viewFacade.changeScreen(ChangeScreenEvent.GotoLobby)
    }
  }


}

