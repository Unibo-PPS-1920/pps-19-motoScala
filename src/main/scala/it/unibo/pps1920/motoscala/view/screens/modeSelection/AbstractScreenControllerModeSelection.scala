package it.unibo.pps1920.motoscala.view.screens.modeSelection

import java.util.function.UnaryOperator

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.model.NetworkAddr
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.{ScreenController, ScreenEvent}
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField, TextFormatter}
import javafx.scene.layout.{AnchorPane, BorderPane}


abstract class AbstractScreenControllerModeSelection(protected override val viewFacade: ViewFacade,
                                                     protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  @FXML protected var root: BorderPane = _
  @FXML protected var mainAnchorPane: AnchorPane = _
  @FXML protected var buttonBack: Button = _
  @FXML protected var buttonHost: Button = _
  @FXML protected var buttonJoin: Button = _
  @FXML protected var ipTextField: TextField = _
  @FXML protected var portTextField: TextField = _
  private var ipReady: Boolean = false
  private var portReady: Boolean = false


  @FXML override def initialize(): Unit = {
    assertNodeInjected()
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

  private def initBackButton(): Unit = {
    buttonBack.setOnAction(_ => viewFacade.changeScreen(ScreenEvent.GoBack))
    buttonHost.setOnAction(_ => {
      viewFacade.changeScreen(ScreenEvent.GotoLobby)
      this.controller.becomeHost()
    })
    buttonJoin.setOnAction(_ => {
      this.controller.tryJoinLobby()
      viewFacade.changeScreen(ScreenEvent.GotoLobby)
    })
  }

  private def initTextField(): Unit = {
    val partialBlock: String = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))"
    val subsequentPartialBlock: String = "(\\." + partialBlock + ")"
    val ipAddressRegex: String = "^" + (partialBlock + "?" + subsequentPartialBlock + "{0,3}")

    val ipAddressFilter: UnaryOperator[javafx.scene.control.TextFormatter.Change] = formatter => {
      var value: String = formatter.getControlNewText
      if (value.matches(ipAddressRegex)) {
        formatter
      } else {
        null
      }
    }
    ipTextField.setTextFormatter(new TextFormatter(ipAddressFilter))

    this.ipTextField.textProperty().addListener((observable, oldValue, newValue) => {
      if (NetworkAddr.validateIPV4Address(newValue)) {
        this.ipReady = true
      } else {
        this.ipReady = false
        this.buttonJoin.setDisable(true)
      }
    })

    this.portTextField.textProperty().addListener((observable, oldValue, newValue) => {
      if (NetworkAddr.validatePort(newValue.toInt)) {
        this.ipReady = true

      } else {
        this.ipReady = false
      }
    })
  }

  private def checkIpAndPort(): Unit = {
    if (this.ipReady && this.portReady) {
      this.buttonJoin.setDisable(true)

    } else {
      this.buttonJoin.setDisable(false)
    }
  }

}

