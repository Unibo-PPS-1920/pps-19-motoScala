package it.unibo.pps1920.motoscala.ecs.entities

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.Entity
/**
 * Beecon entity
 *
 * @param uuid uuid
 */
final case class BeeconEntity(uuid: UUID) extends Entity