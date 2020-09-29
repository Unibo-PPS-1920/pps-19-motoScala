package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.controller.mediation.EntityType
import it.unibo.pps1920.motoscala.ecs.Component

final case class TypeComponent(enType: EntityType) extends Component