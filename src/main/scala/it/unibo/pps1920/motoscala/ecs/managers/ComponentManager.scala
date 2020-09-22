package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.managers.Coordinator.ComponentType
import it.unibo.pps1920.motoscala.ecs.{Component, Entity}

private[managers] trait ComponentManager {
  def registerComponentType(compType: ComponentType): Unit
  def checkRegisteredComponent(compType: ComponentType): Boolean
  def bindComponentToEntity(entity: Entity, component: Component): ECSSignature
  def unbindComponentFromEntity(entity: Entity, component: Component): ECSSignature
  def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component]
  def entityDestroyed(entity: Entity): Unit
}

private[managers] object ComponentManager {
  private class ComponentManagerImpl extends ComponentManager {
    private var registered: Set[ComponentType] = Set()
    private var entityComponent: Map[Entity, Map[ComponentType, Component]] = Map()

    import ImplicitConversions._
    override def registerComponentType(compType: ComponentType): Unit = registered = registered + compType
    override def checkRegisteredComponent(compType: ComponentType): Boolean = registered contains compType
    override def bindComponentToEntity(entity: Entity, component: Component): ECSSignature = {
      require(checkRegisteredComponent(component), "Cannot use an unregistered component")
      entityComponent = entityComponent.updatedWith(entity)(_.fold[Option[Map[ComponentType, Component]]]
        (Option(Map((component, component))))
        (map => Option(map + ((component, component)))))
      ECSSignature(entityComponent(entity).keySet)
    }
    override def unbindComponentFromEntity(entity: Entity, component: Component): ECSSignature = {
      require(checkRegisteredComponent(component), "Cannot use an unregistered component")
      entityComponent = entityComponent.updatedWith(entity)(_.fold[Option[Map[ComponentType, Component]]]
        (Option(Map()))
        (map => Option(map - component)))
      ECSSignature(entityComponent(entity).keySet)
    }
    override def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component] =
      entityComponent.get(entity).flatMap(_.get(compType))
    override def entityDestroyed(entity: Entity): Unit = entityComponent -= entity

    object ImplicitConversions {
      implicit def componentToComponentType(component: Component): ComponentType = component.getClass
    }
  }
  def apply(): ComponentManager = new ComponentManagerImpl()
}