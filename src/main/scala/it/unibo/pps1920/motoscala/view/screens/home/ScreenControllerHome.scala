package it.unibo.pps1920.motoscala.view.screens.home

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import it.unibo.pps1920.motoscala.view.screens.FXMLScreens


final class ScreenControllerHome(protected override val viewFacade: ViewFacade,
                                 protected override val controller: ObservableUI) extends AbstractScreenControllerHome(viewFacade, controller) {
  viewFacade.loadFXMLNode(FXMLScreens.HOME, this)
  logger info "Home Screen"


  override def notify(ev: ViewEvent): Unit = ev match {
    case event: ViewEvent.HomeEvent => logger info "Home message"
    case event: ViewEvent.GameEvent => ???
    case event: ViewEvent.SettingsEvent => ???
    case event: ViewEvent.StatsEvent => ???
    case _ => ???
  }


}