package it.unibo.pps1920.motoscala.ecs.core

import it.unibo.pps1920.motoscala.ecs.core.Coordinator.ComponentType
import it.unibo.pps1920.motoscala.ecs.{Component, Entity}

import scala.reflect.ClassTag

private[core] trait ComponentManager {
  def registerComponentType(compType: ComponentType): Unit
  def checkRegisteredComponent(compType: ComponentType): Boolean
  def bindComponentToEntity(entity: Entity, component: Component): ECSSignature
  def unbindComponentFromEntity(entity: Entity, component: Component): ECSSignature
  def getEntitySignature(entity: Entity): ECSSignature
  def getEntityComponent[T: ClassTag](entity: Entity): T
  def entityDestroyed(entity: Entity): Unit
}

private[core] object ComponentManager {
  private class ComponentManagerImpl extends ComponentManager {
    private var registered: Set[ComponentType] = Set()
    private var entityComponent: Map[Entity, Map[ComponentType, Component]] = Map()

    import ImplicitConversions._
    override def registerComponentType(compType: ComponentType): Unit = registered = registered + compType
    override def checkRegisteredComponent(compType: ComponentType): Boolean = registered contains compType
    override def bindComponentToEntity(entity: Entity, component: Component): ECSSignature = {
      require(checkRegisteredComponent(component), "Cannot use an unregistered component")
      entityComponent = entityComponent.updatedWith(entity)({
        case None => Option(Map((component, component)))
        case Some(map) => Option(map + ((component, component)))
      })
      getEntitySignature(entity)
    }
    override def unbindComponentFromEntity(entity: Entity, component: Component): ECSSignature = {
      require(checkRegisteredComponent(component), "Cannot use an unregistered component")
      entityComponent = entityComponent.updatedWith(entity)({
        case None => Option(Map())
        case Some(map) => Option(map - component)
      })
      getEntitySignature(entity)
    }
    override def getEntityComponent[T: ClassTag](entity: Entity): T =
      entityComponent.get(entity).flatMap(_.get(implicitly[ClassTag[T]].runtimeClass)).get.asInstanceOf[T]
    override def entityDestroyed(entity: Entity): Unit = entityComponent -= entity

    object ImplicitConversions {
      implicit def componentToComponentType(component: Component): ComponentType = component.getClass
    }
    override def getEntitySignature(entity: Entity): ECSSignature = ECSSignature(entityComponent(entity).keySet)
  }
  def apply(): ComponentManager = new ComponentManagerImpl()
}