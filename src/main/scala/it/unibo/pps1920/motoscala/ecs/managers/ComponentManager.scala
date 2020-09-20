package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.managers.ComponentManager.ComponentType
import it.unibo.pps1920.motoscala.ecs.managers.exceptions.ComponentNotRegisteredException
import it.unibo.pps1920.motoscala.ecs.{Component, Entity}

import scala.util.{Failure, Success, Try}
trait ComponentManager {
  def registerComponentType(compType: ComponentType): Unit
  def checkRegisteredComponent(compType: ComponentType): Boolean
  def bindComponentToEntity(entity: Entity, component: Component): Try[(Entity, Component)]
  def unbindComponentFromEntity(entity: Entity, component: Component): Try[(Entity, Component)]
  def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component]
}

object ComponentManager {
  private class ComponentManagerImpl extends ComponentManager {
    private var registered: Set[ComponentType] = Set()
    private var entityComponent: Map[Entity, Map[ComponentType, Component]] = Map()
    override def registerComponentType(compType: ComponentType): Unit = registered = registered + compType
    override def checkRegisteredComponent(compType: ComponentType): Boolean = registered contains compType
    override def bindComponentToEntity(entity: Entity, component: Component): Try[(Entity, Component)] = {
      bindUnbind(entity, component)({
        case Some(map) => Option(map + ((component.getClass, component)))
        case None => Option(Map(component.getClass -> component))
      })
    }
    override def unbindComponentFromEntity(entity: Entity, component: Component): Try[(Entity, Component)] = {
      bindUnbind(entity, component)({
        case Some(map) => Option(map - component.getClass)
        case None => None
      })
    }
    override def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component] =
      entityComponent.get(entity).flatMap(c => c.get(compType))

    private def bindUnbind(entity: Entity, component: Component)
                          (f: Option[Map[ComponentType, Component]] => Option[Map[ComponentType, Component]]): Try[(Entity, Component)] = {
      if (checkRegisteredComponent(component.getClass)) {
        entityComponent = entityComponent.updatedWith(entity)(f)
        Success(entity, component)
      } else {
        Failure(ComponentNotRegisteredException(component))
      }
    }

    object ImplicitConversions {
      implicit def componentToComponentType(component: Component): ComponentType = component.getClass
    }
  }
  type ComponentType = Class[_]
  def apply(): ComponentManager = new ComponentManagerImpl()
}