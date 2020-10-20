package it.unibo.pps1920.motoscala.ecs.components

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.ecs.{Component, Entity}

/**
 *
 * @param mass determines how far the entity will bounce away
 * @param duration keeps track of how long an active collision will last
 * @param oldSpeed original speed to be restored after collision has happened
 */
final case class CollisionComponent(var life: Long,
                                    var mass: Double,
                                    var damage: Int = 1,
                                    var collEntity: Entity = new Entity {override def uuid: UUID = UUID.randomUUID()},
                                    var isColliding: Boolean = false,
                                    var duration: Int = 0,
                                    var oldSpeed: Vector2 = Vector2(0, 0),
                                   ) extends Component

