package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.components.Shape.Shape
import it.unibo.pps1920.motoscala.ecs.util.Direction.Direction
import it.unibo.pps1920.motoscala.ecs.util.Vector2

sealed trait Event

object Event {
  type EntityData = EventData.EntityData
  type LevelSetupData = String
  type LevelEndData = String
  type CommandData = EventData.CommandData
  sealed trait DisplayableEvent extends Event
  final case class DrawEntityEvent(entity: Seq[EntityData]) extends DisplayableEvent
  final case class LevelSetupEvent(data: LevelSetupData) extends DisplayableEvent
  final case class LevelEndEvent(data: LevelEndData) extends DisplayableEvent
  sealed trait CommandableEvent extends Event
  final case class CommandEvent(cmd: Event.CommandData) extends CommandableEvent
}

object EventData {
  final case class CommandData(entity: Entity, direction: Direction)
  final case class EntityData(vec: Vector2, shape: Shape, enType: EntityType)
}