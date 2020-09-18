package it.unibo.pps1920.motoscala.controller.mediation

sealed trait Event
object Event {
  type Entity = String
  type LevelSetupData = String
  type LevelEndData = String
  type CommandData = String
  trait DisplayableEvent extends Event
  case class DrawEntityEvent(entity: Seq[Entity]) extends DisplayableEvent
  case class LevelSetupEvent(data: LevelSetupData) extends DisplayableEvent
  case class LevelEndEvent(data: LevelEndData) extends DisplayableEvent
  trait CommandableEvent extends Event
  case class CommandEvent(cmd: CommandData) extends CommandableEvent
}