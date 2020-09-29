package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EntityData, LevelEndData, LevelSetupData}
import it.unibo.pps1920.motoscala.controller.mediation.{Displayable, Mediator}
import it.unibo.pps1920.motoscala.view.ViewFacade
import it.unibo.pps1920.motoscala.view.events.ViewEvent
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane

class ScreenControllerGame(protected override val viewFacade: ViewFacade,
                           protected override val controller: ObservableUI)
  extends ScreenController(viewFacade, controller) with Displayable {
  private val mediator: Mediator = controller.getMediator
  mediator.subscribe(this)

  @FXML protected var root: BorderPane = _

  @FXML def initialize(): Unit = {
    assert(root != null, "fx:id=\"root\" was not injected: check your FXML file 'Game.fxml'.")
  }
  override def notifyDrawEntities(entities: Seq[EntityData]): Unit = ???
  override def notifyLevelSetup(data: LevelSetupData): Unit = ???
  override def notifyLevelEnd(data: LevelEndData): Unit = ???

  //MVC notify from controller
  override def notify(ev: ViewEvent): Unit = ???
}
