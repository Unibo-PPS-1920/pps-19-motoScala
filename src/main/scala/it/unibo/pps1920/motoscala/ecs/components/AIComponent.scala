package it.unibo.pps1920.motoscala.ecs.components

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.Component
/**
 * Component that defines ai behaviour
 *
 * @param foolishness the more foolish an entity is the more randomly he will move
 * @param target the player he will target
 */
case class AIComponent(foolishness: Int, var target: UUID) extends Component
