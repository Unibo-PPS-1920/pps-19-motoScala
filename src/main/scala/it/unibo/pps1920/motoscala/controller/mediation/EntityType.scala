package it.unibo.pps1920.motoscala.controller.mediation

sealed trait EntityType

object EntityType {

  case object Player extends EntityType
  case object Tile extends EntityType
  sealed trait Enemy extends EntityType
  case object Enemy1 extends Enemy
}
