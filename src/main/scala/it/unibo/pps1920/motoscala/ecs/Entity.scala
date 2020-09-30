package it.unibo.pps1920.motoscala.ecs

import java.util.UUID


sealed trait Entity {
  def uuid: UUID
}
