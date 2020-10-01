package it.unibo.pps1920.motoscala.view.screens.game

import cats.syntax.option._
import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData.CommandData
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.util.Direction
import it.unibo.pps1920.motoscala.ecs.util.Direction._
import javafx.event.EventHandler
import javafx.scene.input.{KeyCode, KeyEvent}
import javafx.scene.layout.Pane
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable

class GameEventHandler {
  protected val logger: Logger = LoggerFactory getLogger this.getClass
  private val activeKeys: mutable.Map[KeyCode, Boolean] = mutable.Map()
  private val keyPressedHandler: EventHandler[KeyEvent] = createKeyPressHandler()
  private val keyReleasedHandler: EventHandler[KeyEvent] = createKeyReleasedHandler()
  private var handleCommand: Option[CommandEvent => Unit] = None
  private var entity: Option[Entity] = None

  def addKeyListeners(en: Entity)(root: Pane, f: CommandEvent => Unit): Unit = {
    root.addEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler)
    root.addEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler)
    handleCommand = f.some
    entity = en.some
  }
  def removeKeyListeners(root: Pane): Unit = {
    root.removeEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler)
    root.removeEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler)
    handleCommand = None
  }

  private def createKeyPressHandler(): EventHandler[KeyEvent] = {
    val keyPressedHandler: EventHandler[KeyEvent] = e => {
      val key = e.getCode
      if (!activeKeys.getOrElseUpdate(key, true)) {
        key match {
          case KeyCode.LEFT => setKey(key, West, isActive = true)
          case KeyCode.RIGHT => setKey(key, East, isActive = true)
          case KeyCode.UP => setKey(key, North, isActive = true)
          case KeyCode.DOWN => setKey(key, South, isActive = true)
          case _ => logger info ""
        }
      }
    }
    keyPressedHandler
  }
  private def createKeyReleasedHandler(): EventHandler[KeyEvent] = {
    val keyReleasedHandler: EventHandler[KeyEvent] = e => {
      val key = e.getCode
      if (activeKeys.getOrElseUpdate(key, false)) {
        key match {
          case KeyCode.LEFT => setKey(key, West, isActive = false)
          case KeyCode.RIGHT => setKey(key, East, isActive = false)
          case KeyCode.UP => setKey(key, North, isActive = false)
          case KeyCode.DOWN => setKey(key, South, isActive = false)
          case _ => logger info ""
        }
      }
    }
    keyReleasedHandler
  }

  private def setKey(keyCode: KeyCode, direction: Direction, isActive: Boolean): Unit = {
    activeKeys += (keyCode -> isActive)
    handleCommand.foreach(_ (CommandEvent(CommandData(entity.get, direction, isActive))))

    logger info s"${if (isActive) "pressed" else "released"} $direction"
  }
}

