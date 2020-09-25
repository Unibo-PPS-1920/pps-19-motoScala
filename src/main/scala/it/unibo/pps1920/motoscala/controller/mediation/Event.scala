package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.ecs.components.Shape.Shape
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import scalafx.scene.paint.Color

sealed trait Event

object Event {


  type Entity = (Vector2, Shape, Color)
  type LevelSetupData = String
  type LevelEndData = String
  type CommandData = String

  sealed trait DisplayableEvent extends Event

  final case class DrawEntityEvent(entity: Seq[Entity]) extends DisplayableEvent

  final case class LevelSetupEvent(data: LevelSetupData) extends DisplayableEvent

  final case class LevelEndEvent(data: LevelEndData) extends DisplayableEvent

  sealed trait CommandableEvent extends Event

  final case class CommandEvent(cmd: CommandData) extends CommandableEvent

}