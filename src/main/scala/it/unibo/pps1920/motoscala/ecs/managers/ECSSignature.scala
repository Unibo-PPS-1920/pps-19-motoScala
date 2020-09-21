package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.Component
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator.ComponentType

case class ECSSignature(types: ComponentType*) {
  private var signature: Set[ComponentType] = Set()
  signature = signature ++ types

  def signComponent(component: Component*): ECSSignature = {
    signature = signature ++ component.map(_.getClass)
    this
  }
  def repudiateComponent(component: Component*): ECSSignature = {
    signature = signature -- component.map(_.getClass)
    this
  }
}
