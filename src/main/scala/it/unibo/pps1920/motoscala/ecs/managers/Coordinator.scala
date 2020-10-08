package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.managers.Coordinator.ComponentType
import it.unibo.pps1920.motoscala.ecs.{Component, Entity, System}

trait Coordinator {
  def addEntity(entity: Entity): Coordinator
  def removeEntity(entity: Entity): Coordinator
  def registerComponentType(compType: ComponentType): Coordinator
  def addEntityComponent(entity: Entity, component: Component): Coordinator
  def removeEntityComponent(entity: Entity, component: Component): Coordinator
  def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component]
  def registerSystem(sys: System): Coordinator
  def updateSystems(): Unit
}

object Coordinator {
  private class CoordinatorImpl() extends Coordinator {
    private val componentManager = ComponentManager()
    private val systemManager = SystemManager()
    private val entityManager = EntityManager()

    override def addEntity(entity: Entity): Coordinator = {
      entityManager addEntity entity
      this
    }
    override def removeEntity(entity: Entity): Coordinator = {
      entityManager removeEntity entity
      systemManager entityDestroyed entity
      componentManager entityDestroyed entity
      this
    }
    override def registerComponentType(compType: ComponentType): Coordinator = {
      componentManager.registerComponentType(compType)
      this
    }
    override def addEntityComponent(entity: Entity, component: Component): Coordinator = {
      this.synchronized(systemManager.entitySignatureChanged(entity, componentManager
        .bindComponentToEntity(entity, component)))
      this
    }
    override def removeEntityComponent(entity: Entity, component: Component): Coordinator = {

      this.synchronized(systemManager.entitySignatureChanged(entity, componentManager
        .unbindComponentFromEntity(entity, component)))
      this
    }
    override def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component] =
      this.synchronized(componentManager.getEntityComponent(entity, compType))
    override def registerSystem(sys: System): Coordinator = {
      this.synchronized(systemManager.registerSystem(sys))
      this
    }
    override def updateSystems(): Unit = this.synchronized(systemManager.updateAll())
  }
  type ComponentType = Class[_]
  def apply(): Coordinator = new CoordinatorImpl()
}
