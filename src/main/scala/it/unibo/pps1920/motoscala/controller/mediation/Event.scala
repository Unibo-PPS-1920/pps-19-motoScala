package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent
import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.components.Shape
import it.unibo.pps1920.motoscala.ecs.util.{Direction, Vector2}

/** A Mediator Event is exchanged between [[it.unibo.pps1920.motoscala.controller.mediation.EventObserver]] */
sealed trait Event
object Event {
  type EntityData = EventData.EntityData
  type LifeData = EventData.LifeData
  type SoundEvent = MediaEvent
  type EndData = EventData.EndData
  type CommandData = EventData.CommandData
  /** Event that represent display actions. It is sent to Server-View and Client-Views from server's Engine */
  sealed trait DisplayableEvent extends Event
  final case class DrawEntityEvent(players: Set[Option[EntityData]], entity: Set[EntityData]) extends DisplayableEvent
  final case class EntityLifeEvent(data: LifeData) extends DisplayableEvent
  final case class RedirectSoundEvent(event: SoundEvent) extends DisplayableEvent
  final case class LevelEndEvent(data: EndData) extends DisplayableEvent
  /** Event that represent command actions. It is sent to server's Engine from Server-View and Client-Views */
  sealed trait CommandableEvent extends Event
  final case class CommandEvent(cmd: Event.CommandData) extends CommandableEvent
}
/** Data sent in [[Event]] */
object EventData {
  final case class EntityData(pos: Vector2, direction: Direction, shape: Shape, entity: Entity)
  final case class LifeData(entity: Entity, life: Int)
  final case class EndData(hasWon: Boolean, entity: Entity, score: Int)
  final case class CommandData(entity: Entity, direction: Direction)
}