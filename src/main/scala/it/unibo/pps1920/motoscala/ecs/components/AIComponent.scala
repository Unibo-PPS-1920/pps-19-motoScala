package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity

import scala.collection.mutable
/**
 * Component that defines ai behaviour
 *
 * @param foolishness the more foolish an entity is the more randomly he will move
 * @param targets possible available targets
 */

case class AIComponent(foolishness: Int, targets: mutable.Stack[BumperCarEntity]) extends Component
