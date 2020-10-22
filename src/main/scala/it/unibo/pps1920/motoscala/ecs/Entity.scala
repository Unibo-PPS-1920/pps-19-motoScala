package it.unibo.pps1920.motoscala.ecs

import java.util.UUID

/** Represents an Entity in ECS framework */
trait Entity {
  def uuid: UUID
}
