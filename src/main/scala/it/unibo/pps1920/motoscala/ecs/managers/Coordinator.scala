package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.managers.Coordinator.ComponentType
import it.unibo.pps1920.motoscala.ecs.{Component, Entity, System}

trait Coordinator {
  def addEntity(entity: Entity): Unit
  def removeEntity(entity: Entity): Unit
  def registerComponentType(compType: ComponentType): Unit
  def addEntityComponent(entity: Entity, component: Component): Unit
  def removeEntityComponent(entity: Entity, component: Component): Unit
  def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component]
  def registerSystem(sys: System): Unit
  def updateSystems(): Unit
}

object Coordinator {
  private class CoordinatorImpl() extends Coordinator {
    private val componentManager = ComponentManager()
    private val systemManager = SystemManager()
    private val entityManager = EntityManager()

    override def addEntity(entity: Entity): Unit = entityManager addEntity entity
    override def removeEntity(entity: Entity): Unit = {
      entityManager removeEntity entity
      systemManager entityDestroyed entity
      componentManager entityDestroyed entity
    }
    override def registerComponentType(compType: ComponentType): Unit = componentManager.registerComponentType(compType)
    override def addEntityComponent(entity: Entity, component: Component): Unit =
      this.synchronized(systemManager.entitySignatureChanged(entity, componentManager
        .bindComponentToEntity(entity, component)))
    override def removeEntityComponent(entity: Entity, component: Component): Unit =
      this.synchronized(systemManager.entitySignatureChanged(entity, componentManager
        .unbindComponentFromEntity(entity, component)))
    override def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component] =
      this.synchronized(componentManager.getEntityComponent(entity, compType))
    override def registerSystem(sys: System): Unit = this.synchronized(systemManager.registerSystem(sys))
    override def updateSystems(): Unit = this.synchronized(systemManager.updateAll())
  }
  type ComponentType = Class[_]
  def apply(): Coordinator = new CoordinatorImpl()
}
