package it.unibo.pps1920.motoscala.controller.mediation


import it.unibo.pps1920.motoscala.controller.mediation.EventData.LevelSetupData
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.components.Shape
import it.unibo.pps1920.motoscala.ecs.util.{Direction, Vector2}
import it.unibo.pps1920.motoscala.model.Level.LevelData

sealed trait Event

object Event {
  type EntityData = EventData.DrawEntityData
  type LevelEndData = String
  type CommandData = EventData.CommandData
  sealed trait DisplayableEvent extends Event
  final case class DrawEntityEvent(player: EntityData, entity: Seq[EntityData]) extends DisplayableEvent
  final case class LevelSetupEvent(data: LevelSetupData) extends DisplayableEvent
  final case class LevelEndEvent(data: LevelEndData) extends DisplayableEvent
  sealed trait CommandableEvent extends Event
  final case class CommandEvent(cmd: Event.CommandData) extends CommandableEvent
}

object EventData {
  final case class CommandData(entity: Entity, direction: Direction, moving: Boolean)
  final case class DrawEntityData(pos: Vector2, direction: Direction, shape: Shape, entity: Entity)
  final case class LevelSetupData(level: LevelData, isSinglePlayer: Boolean, isHosting: Boolean, playerEntity: Entity)
}