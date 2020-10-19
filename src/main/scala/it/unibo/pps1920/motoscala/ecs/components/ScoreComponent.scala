package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component

/**
 * Component for holding the score a player will receive by eliminating the associated entity
 *
 * @param score score value
 */
case class ScoreComponent(score: Int) extends Component
