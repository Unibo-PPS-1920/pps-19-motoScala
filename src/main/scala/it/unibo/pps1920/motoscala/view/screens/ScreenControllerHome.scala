package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent


final class ScreenControllerHome(protected override val viewFacade: ViewFacade,
                                 protected override val controller: ObservableUI) extends AbstractScreenControllerHome(viewFacade, controller) {
  viewFacade.loadFXMLNode(FXMLScreens.HOME, this)
  logger info "Home Screen"

  this.textPlay.setOnAction(_ => {
    viewFacade.changeScreen(ScreenEvent.GotoGame)
  })

  this.textPlayMultiplayer.setOnAction(_ => {
    viewFacade.changeScreen(ScreenEvent.GotoLobby)
  })

  this.textSettings.setOnAction(_ => {
    viewFacade.changeScreen(ScreenEvent.GotoSettings)
  })

  this.textStats.setOnAction(_ => {
    viewFacade.changeScreen(ScreenEvent.GotoStats)
  })


  this.textExit.setOnAction(_ => {
    import javafx.application.Platform
    Platform.exit()
  })


  override def notify(ev: ViewEvent): Unit = ev match {
    case event: ViewEvent.HomeEvent => logger info "Home message"
    case event: ViewEvent.GameEvent => ???
    case event: ViewEvent.SettingsEvent => ???
    case event: ViewEvent.StatsEvent => ???
    case _ => ???
  }


}
