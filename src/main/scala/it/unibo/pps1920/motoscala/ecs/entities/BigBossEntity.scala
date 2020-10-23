package it.unibo.pps1920.motoscala.ecs.entities

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.Entity

/**
 * Big Boss entity
 *
 * @param uuid uuid
 */
final case class BigBossEntity(uuid: UUID) extends Entity
