package it.unibo.pps1920.motoscala.view.screens.game

import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.util.Direction
import it.unibo.pps1920.motoscala.ecs.util.Direction._
import javafx.event.EventHandler
import javafx.scene.input.KeyCode._
import javafx.scene.input.{KeyCode, KeyEvent}
import javafx.scene.layout.Pane
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable

case class GameEventHandler(
  private var pane: Pane,
  private var handleCommand: CommandEvent => Unit,
  private var entity: Entity
) {
  protected val logger: Logger = LoggerFactory getLogger this.getClass
  private val activeKeys: mutable.HashSet[Direction] = mutable.HashSet()
  private val keyPressedHandler: EventHandler[KeyEvent] = createKeyPressHandler()
  private val keyReleasedHandler: EventHandler[KeyEvent] = createKeyReleasedHandler()

  pane.addEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler)
  pane.addEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler)

  def dismiss(): Unit = {
    pane.removeEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler)
    pane.removeEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler)
  }

  import ImplicitConversions._
  private def createKeyPressHandler(): EventHandler[KeyEvent] = e => {
    val dir = e.getCode
    if (!activeKeys.contains(dir)) {
      activeKeys add dir
      update()
    }
  }

  private def createKeyReleasedHandler(): EventHandler[KeyEvent] = e => {
    val dir = e.getCode
    if (activeKeys.contains(dir)) {
      activeKeys remove dir
      update()
    }
  }

  private def update(): Unit = {
    val dir = activeKeys.foldLeft[Direction](Center)(_ + _)
    handleCommand(CommandEvent(CommandData(entity, dir)))
  }

  object ImplicitConversions {
    implicit def keyToDir(key: KeyCode): Direction = key match {
      case W => North
      case S => South
      case A => West
      case D => East
      case _ => Center
    }
  }
}


