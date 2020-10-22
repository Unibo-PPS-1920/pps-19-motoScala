package it.unibo.pps1920.motoscala.view.screens.lobby

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{SplitMenuButton, _}
import javafx.scene.layout.BorderPane
import scalafx.scene.paint.Color

/** Abstract ScreenController dedicated to show the multiplayer lobby.
 *
 * @param viewFacade the view facade
 * @param controller the controller
 */
protected[lobby] abstract class AbstractScreenControllerLobby(
  protected override val viewFacade: ViewFacade,
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
    listPlayer.getSelectionModel.selectedItemProperty().addListener((_, _, newVal) => {
      if (newVal != null && listPlayer.getItems.get(0) != newVal)
        buttonKick.setDisable(false)
      else
        buttonKick.setDisable(true)
    })
  }

  private def initButtons(): Unit = {


    buttonReady.setOnAction(_ => controller.lobbyInfoChanged(isStatusChanged = true))
    buttonKick.setOnAction(_ => controller.kickSomeone(listPlayer.getSelectionModel.getSelectedItem.getText))
    buttonStart.setOnAction(_ => {
      controller.startMultiplayer()
      viewFacade.changeScreen(ChangeScreenEvent.GotoGame)
    })
    buttonReady.setDisable(false)
  }

  private def extendButtonBackBehaviour(): Unit = {
    buttonBack.addEventHandler[ActionEvent](ActionEvent.ACTION, _ => {
      controller.leaveLobby()
      cleanAll()
    })
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


  protected def leaveLobby(): Unit = {
    Platform.runLater(() => {
      viewFacade.changeScreen(ChangeScreenEvent.GoBack)
    })
  }

  protected def updateLobby(lobbyData: LobbyData): Unit = {
    Platform.runLater(() => {

      lobbyData.difficulty.foreach(diff => dropMenuDifficult.setText(diff.toString))
      lobbyData.level.foreach(lvl => dropMenuLevel.setText(lvl.toString))


      val rpValues = lobbyData.readyPlayers.values

      if (rpValues.nonEmpty && (rpValues.map(pd => pd.status).count(s => !s) == 0) && rpValues.size > 1) {
        buttonStart.setDisable(false)
      } else {
        buttonStart.setDisable(true)
      }

      val lisPlayerStatus = listPlayer.getItems
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

  protected def startMulti(): Unit = {
    viewFacade.changeScreen(ChangeScreenEvent.GotoGame)
  }

  protected def setIpAndPort(ip: String, port: String, name: String, levels: List[Int],
                             difficulties: List[Int]): Unit = {
    import scala.jdk.CollectionConverters._
    reset()


    dropMenuLevel.getItems.addAll(levels.map(lvl => new MenuItem(lvl.toString)).asJava)
    dropMenuDifficult.getItems.addAll(difficulties.map(diff => new MenuItem(diff.toString)).asJava)

    def initSplitMenuButton(butt: SplitMenuButton)(controllerStrategy: (ObservableUI, Option[Int]) => Unit): Unit = {
      butt.getItems.forEach(_.setOnAction(el => {
        val lvl = el.getSource.asInstanceOf[MenuItem].getText
        dropMenuLevel.setText(lvl)
        controllerStrategy(controller, Some(lvl.toInt))
      }))
    }
    initSplitMenuButton(dropMenuLevel)((controller, lvl) => {
      controller.lobbyInfoChanged(level = lvl)
    })
    initSplitMenuButton(dropMenuDifficult)((controller, diff) => {
      controller.lobbyInfoChanged(difficult = diff)
    })

    controller.lobbyInfoChanged(level = Some(1), difficult = Some(1))


    dropMenuLevel.setText(levels.head.toString)
    dropMenuDifficult.setText(difficulties.head.toString)

    ipLabel.setText(s"${ipLabel.getText}$ip")
    portLabel.setText(s"${portLabel.getText} $port")
    val label = new Label(name)
    label.setTextFill(Color.Red)
    listPlayer.getItems.add(label)
  }

  private def reset(): Unit = {

    cleanAll()
    dropMenuDifficult.getItems.clear()
    dropMenuLevel.getItems.clear()
    listPlayer.getItems.clear()
    buttonStart.setVisible(true)
    buttonKick.setVisible(true)
    ipLabel.setVisible(true)
    portLabel.setVisible(true)
    dropMenuDifficult.setDisable(false)
    dropMenuLevel.setDisable(false)
  }

  private def cleanAll(): Unit = {
    buttonKick.setDisable(true)
    buttonStart.setDisable(true)
    buttonKick.setVisible(false)
    buttonStart.setVisible(false)
    ipLabel.setVisible(false)
    portLabel.setVisible(false)
    dropMenuDifficult.setDisable(true)
    dropMenuLevel.setDisable(true)
    listPlayer.getItems.clear()
    ipLabel.setText("Ip:")
    portLabel.setText("Port:")
  }

}

