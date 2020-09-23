package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.UpdatableUI
import it.unibo.pps1920.motoscala.view.{ObserverUI, ViewFacade}
import javafx.fxml.FXML
import org.slf4j.{Logger, LoggerFactory}

abstract class ScreenController(protected val viewFacade: ViewFacade,
                                protected val controller: UpdatableUI) extends ObserverUI {
  protected val logger: Logger = LoggerFactory getLogger this.getClass
  @FXML def initialize(): Unit
}
