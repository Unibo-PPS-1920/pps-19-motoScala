package it.unibo.pps1920.motoscala.view.screens.lobby

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.layout.BorderPane
import scalafx.scene.paint.Color


abstract class AbstractScreenControllerLobby(protected override val viewFacade: ViewFacade,
                                             protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  @FXML protected var root: BorderPane = _
  @FXML protected var mainBorderPane: BorderPane = _
  @FXML protected var buttonKick: Button = _
  @FXML protected var buttonReady: Button = _
  @FXML protected var buttonStart: Button = _
  @FXML protected var dropMenuDifficult: SplitMenuButton = _
  @FXML protected var dropMenuLevel: SplitMenuButton = _
  @FXML protected var listPlayer: ListView[Label] = _
  @FXML protected var ipLabel: Label = _
  @FXML protected var portLabel: Label = _

  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    extendButtonBackBehaviour()
    initBackButton()
    initButtons()
    initSplitMenus()
  }
  private def initSplitMenus(): Unit = {
    this.listPlayer.getSelectionModel.selectedItemProperty().addListener((_, _, newVal) => {
      if (newVal != null) {
        if (this.listPlayer.getItems.get(0) != newVal) {
          this.buttonKick.setDisable(false)
        } else {
          this.buttonKick.setDisable(true)
        }
      } else {
        this.buttonKick.setDisable(true)
      }
    })
  }
  private def initButtons(): Unit = {

    buttonReady.setOnAction(_ => {
      controller.setSelfReady()
    })
    buttonKick.setOnAction(_ => {
      controller.kickSomeone(this.listPlayer.getSelectionModel.getSelectedItem.getText)
    })
    buttonStart.setOnAction(_ => {
      controller.startMultiplayer()
      viewFacade.changeScreen(ChangeScreenEvent.GotoGame)
    })
    this.buttonReady.setDisable(false)
  }
  private def extendButtonBackBehaviour(): Unit = {
    buttonBack.addEventHandler[ActionEvent](ActionEvent.ACTION, _ => {
      this.controller.leaveLobby()
      cleanAll()
    })
  }
  private def cleanAll(): Unit = {
    this.buttonKick.setDisable(true)
    this.buttonStart.setDisable(true)
    this.buttonKick.setVisible(false)
    this.buttonStart.setVisible(false)
    this.ipLabel.setVisible(false)
    this.portLabel.setVisible(false)
    this.dropMenuDifficult.setDisable(true)
    this.dropMenuLevel.setDisable(true)
    this.listPlayer.getItems.clear()
    this.ipLabel.setText("Ip:")
    this.portLabel.setText("Port:")
  }
  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(mainBorderPane != null, "fx:id=\"mainBorderPane\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(buttonBack != null, "fx:id=\"buttonBack\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(buttonKick != null, "fx:id=\"buttonKick\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(buttonReady != null, "fx:id=\"buttonReady\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(buttonStart != null, "fx:id=\"buttonStart\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(dropMenuDifficult != null, "fx:id=\"dropMenuDifficult\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(dropMenuLevel != null, "fx:id=\"dropMenuLevel\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(listPlayer != null, "fx:id=\"listPlayer\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(ipLabel != null, "fx:id=\"ipLabel\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(portLabel != null, "fx:id=\"portLabel\" was not injected: check your FXML file 'Lobby.fxml'.")

  }

  def updateLobby(lobbyData: LobbyData): Unit = {
    Platform.runLater(() => {
      lobbyData.difficulty.foreach(diff => this.dropMenuDifficult.setText("1"))

      val rpValues = lobbyData.readyPlayers.values

      if (rpValues.nonEmpty && (rpValues.map(pd => pd.status).count(s => !s) == 0)) {

        this.buttonStart.setDisable(false)
      } else {
        this.buttonStart.setDisable(true)
      }

      val lisPlayerStatus = this.listPlayer.getItems
      lisPlayerStatus.clear()
      lobbyData.readyPlayers.values.foreach(player => {
        val label = new Label(player.name)
        if (player.status) {
          label.setTextFill(Color.Green)
        } else {
          label.setTextFill(Color.Red)
        }
        lisPlayerStatus.add(label)
      })
    })


  }
  def leaveLobby(): Unit = {
    Platform.runLater(() => {
      viewFacade.changeScreen(ChangeScreenEvent.GoBack)
    })
  }
  protected def startMulti(): Unit = {
    this.viewFacade.changeScreen(ChangeScreenEvent.GotoGame)
  }
  protected def setIpAndPort(ip: String, port: String, name: String): Unit = {
    reset()
    this.ipLabel.setText(s"${this.ipLabel.getText}$ip")
    this.portLabel.setText(s"${this.portLabel.getText} $port")
    val label = new Label(name)
    label.setTextFill(Color.Red)
    this.listPlayer.getItems.add(label)

  }
  private def reset(): Unit = {
    cleanAll()
    this.listPlayer.getItems.clear()
    this.buttonStart.setVisible(true)
    this.buttonKick.setVisible(true)
    this.ipLabel.setVisible(true)
    this.portLabel.setVisible(true)
    this.dropMenuDifficult.setDisable(false)
    this.dropMenuLevel.setDisable(false)
  }


}

