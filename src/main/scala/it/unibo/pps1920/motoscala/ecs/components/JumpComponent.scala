package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component

/**
 * Component for determining if an entity is jumping
 *
 * @param isActive true if the entity is involved in a jump
 */
case class JumpComponent(var isActive: Boolean = false) extends Component
