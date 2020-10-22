package it.unibo.pps1920.motoscala.view.screens.lobby

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.multiplayer.messages.DataType.LobbyData
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.screens.ScreenController
import it.unibo.pps1920.motoscala.view.utilities.ViewUtils.addButtonMusic
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{SplitMenuButton, _}
import javafx.scene.layout.BorderPane
import scalafx.scene.paint.Color

import scala.jdk.CollectionConverters._

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

  override def whenDisplayed(): Unit = {}

  @FXML override def initialize(): Unit = {
    assertNodeInjected(root, mainBorderPane, buttonKick, buttonReady, buttonStart)
    extendButtonBackBehaviour()
    initBackButton()
    initButtons()
    initSplitMenus()
  }

  private def initSplitMenus(): Unit = {
    listPlayer.getSelectionModel.selectedItemProperty().addListener((_, _, newVal) => {
      if (newVal != null && listPlayer.getItems.get(0) != newVal) buttonKick.setDisable(false)
      else buttonKick.setDisable(true)
    })
  }

  private def initButtons(): Unit = {
    addButtonMusic(buttonReady, buttonKick, buttonStart)(controller)
    buttonReady.setOnAction(_ => controller.lobbyInfoChanged(isStatusChanged = true))
    buttonKick.setOnAction(_ => controller.kickSomeone(listPlayer.getSelectionModel.getSelectedItem.getText))
    buttonStart.setOnAction(_ => {
      controller.startMultiplayerGame()
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
  protected def leaveLobby(): Unit = Platform.runLater(() => viewFacade.changeScreen(ChangeScreenEvent.GoBack))
  protected def updateLobby(lobbyData: LobbyData): Unit = {
    Platform.runLater(() => {
      lobbyData.difficulty.foreach(diff => dropMenuDifficult.setText(diff.toString))
      lobbyData.level.foreach(lvl => dropMenuLevel.setText(lvl.toString))
      val rpValues = lobbyData.readyPlayers.values
      if (rpValues.nonEmpty && (rpValues.map(pd => pd.status).count(s => !s) == 0) && rpValues.size > 1)
        buttonStart.setDisable(false)
      else buttonStart.setDisable(true)
      val lisPlayerStatus = listPlayer.getItems
      lisPlayerStatus.clear()


      /*      lobbyData.readyPlayers.values.foreach(player => {
              val label = new Label(player.name)
              if (player.status) {
                label.setTextFill(Color.Green)
              } else {
                label.setTextFill(Color.Red)
              }
              lisPlayerStatus.add(label)
            })*/

      val x: java.util.List[Label] = lobbyData.readyPlayers.values.map(player => {
        val label = new Label(player.name)
        if (player.status) label.setTextFill(Color.Green) else label.setTextFill(Color.Red)
        label
      }).toList.asJava
      lisPlayerStatus.addAll(x)
    })
  }
  protected def startMulti(): Unit = viewFacade.changeScreen(ChangeScreenEvent.GotoGame)
  protected def setIpAndPort(ip: String, port: String, name: String, levels: List[Int],
                             difficulties: List[Int]): Unit = {
    reset()

    dropMenuLevel.getItems.addAll(levels.map(lvl => new MenuItem(lvl.toString)).asJava)
    dropMenuDifficult.getItems.addAll(difficulties.map(diff => new MenuItem(diff.toString)).asJava)

    def initSplitMenuButton(dropMenu: SplitMenuButton)
                           (controllerStrategy: (ObservableUI, Option[Int]) => Unit): Unit = {
      dropMenu.getItems.forEach(_.setOnAction(el => {
        val lvl = el.getSource.asInstanceOf[MenuItem].getText
        dropMenu.setText(lvl)
        controllerStrategy(controller, Some(lvl.toInt))
      }))
    }
    initSplitMenuButton(dropMenuLevel)((controller, lvl) => controller.lobbyInfoChanged(level = lvl))
    initSplitMenuButton(dropMenuDifficult)((controller, diff) => controller.lobbyInfoChanged(difficult = diff))

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

