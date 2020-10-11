package it.unibo.pps1920.motoscala.view.screens.lobby

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.model.ReadyTable.ReadyPlayers
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.screens.{ScreenController, ScreenEvent}
import javafx.fxml.FXML
import javafx.scene.control.{Button, ListView, SplitMenuButton}
import javafx.scene.layout.BorderPane

abstract class AbstractScreenControllerLobby(protected override val viewFacade: ViewFacade,
                                             protected override val controller: ObservableUI) extends ScreenController(viewFacade, controller) {
  @FXML protected var root: BorderPane = _
  @FXML protected var mainBorderPane: BorderPane = _
  @FXML protected var buttonBack: Button = _
  @FXML protected var buttonKick: Button = _
  @FXML protected var buttonReady: Button = _
  @FXML protected var buttonStart: Button = _
  @FXML protected var dropMenuSkin: SplitMenuButton = _
  @FXML protected var dropMenuDifficult: SplitMenuButton = _
  @FXML protected var dropMenuLevel: SplitMenuButton = _
  @FXML protected var listPlayer: ListView[String] = _
  @FXML override def initialize(): Unit = {
    assertNodeInjected()
    initBackButton()
    initButtons()
    initSplitMenus()
  }
  private def initSplitMenus(): Unit = {

  }
  private def initButtons(): Unit = {
    buttonStart.setOnAction(_ => viewFacade.changeScreen(ScreenEvent.GotoGame))
    buttonReady.setOnAction(_ => {
      controller.setSelfReady()
    })
    buttonKick.setOnAction(_ => {
      controller.kickSomeone()
    })
  }

  private def initBackButton(): Unit = {
    buttonBack.setOnAction(_ => viewFacade.changeScreen(ScreenEvent.GoBack))
  }
  private def assertNodeInjected(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(mainBorderPane != null, "fx:id=\"mainBorderPane\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(buttonBack != null, "fx:id=\"buttonBack\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(buttonKick != null, "fx:id=\"buttonKick\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(buttonReady != null, "fx:id=\"buttonReady\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(buttonStart != null, "fx:id=\"buttonStart\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(dropMenuSkin != null, "fx:id=\"dropMenuSkin\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(dropMenuDifficult != null, "fx:id=\"dropMenuDifficult\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(dropMenuLevel != null, "fx:id=\"dropMenuLevel\" was not injected: check your FXML file 'Lobby.fxml'.")
    assert(listPlayer != null, "fx:id=\"listPlayer\" was not injected: check your FXML file 'Lobby.fxml'.")

  }

  protected def updatePlayers(playersStatus: ReadyPlayers): Unit = {
    /*
        this.listPlayer.getItems.set()
    */
  }


}

