package it.unibo.pps1920.motoscala.ecs.components

import java.util.UUID

import it.unibo.pps1920.motoscala.ecs.Component

case class AIComponent(skill: Int, target: UUID) extends Component
