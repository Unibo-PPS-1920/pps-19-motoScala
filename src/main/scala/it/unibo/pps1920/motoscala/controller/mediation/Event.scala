package it.unibo.pps1920.motoscala.controller.mediation


import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.components.Shape
import it.unibo.pps1920.motoscala.ecs.util.{Direction, Vector2}

sealed trait Event

object Event {

  type LevelEndData = EventData.EndData
  type CommandData = EventData.CommandData
  type EntityData = EventData.DrawEntityData
  type SoundEvent = MediaEvent
  sealed trait DisplayableEvent extends Event
  final case class DrawEntityEvent(players: Set[Option[EntityData]], entity: Set[EntityData]) extends DisplayableEvent
  final case class LevelEndEvent(data: LevelEndData) extends DisplayableEvent
  final case class RedirectSoundEvent(event: SoundEvent) extends DisplayableEvent
  sealed trait CommandableEvent extends Event
  final case class CommandEvent(cmd: Event.CommandData) extends CommandableEvent
}

object EventData {
  final case class EndData(hasWon: Boolean, entity: Entity, score: Int)
  final case class CommandData(entity: Entity, direction: Direction)
  final case class DrawEntityData(pos: Vector2, direction: Direction, shape: Shape, entity: Entity)
}