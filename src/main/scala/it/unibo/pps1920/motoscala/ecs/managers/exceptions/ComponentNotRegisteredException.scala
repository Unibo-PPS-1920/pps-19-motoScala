package it.unibo.pps1920.motoscala.ecs.managers.exceptions

import it.unibo.pps1920.motoscala.ecs.Component

case class ComponentNotRegisteredException(component: Component) extends Exception(s"Component $component not registered")