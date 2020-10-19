package it.unibo.pps1920.motoscala.view.screens

import it.unibo.pps1920.motoscala.controller.ObservableUI
import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.view.fsm.ChangeScreenEvent
import it.unibo.pps1920.motoscala.view.{ObserverUI, ViewFacade}
import javafx.fxml.FXML
import javafx.scene.control.Button
import org.slf4j.{Logger, LoggerFactory}

abstract class ScreenController(protected val viewFacade: ViewFacade,
                                protected val controller: ObservableUI) extends ObserverUI {
  protected val logger: Logger = LoggerFactory getLogger this.getClass
  @FXML protected var buttonBack: Button = _
  def whenDisplayed(): Unit
  @FXML def initialize(): Unit
  def initBackButton(): Unit = {
    buttonBack.setOnAction(_ => {
      controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonClick))
      viewFacade.changeScreen(ChangeScreenEvent.GoBack)
    })
    buttonBack.setOnMouseEntered(_ => controller.redirectSoundEvent(PlaySoundEffect(Clips.ButtonHover)))
  }
}
