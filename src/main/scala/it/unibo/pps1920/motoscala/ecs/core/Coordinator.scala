package it.unibo.pps1920.motoscala.ecs.core

import it.unibo.pps1920.motoscala.ecs.core.Coordinator.ComponentType
import it.unibo.pps1920.motoscala.ecs.{Component, Entity, System}

import scala.reflect.ClassTag

trait Coordinator {
  def addEntity(entity: Entity): Coordinator
  def removeEntity(entity: Entity): Coordinator
  def isEntityAlive(entity: Entity): Boolean
  def registerComponentType(compType: ComponentType): Coordinator
  def addEntityComponent(entity: Entity, component: Component): Coordinator
  def addEntityComponents(entity: Entity, components: Component*): Coordinator
  def removeEntityComponent(entity: Entity, component: Component): Coordinator
  def getEntityComponent[T: ClassTag](entity: Entity): T
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
    override def isEntityAlive(entity: Entity): Boolean = entityManager.entities.contains(entity)
    override def registerComponentType(compType: ComponentType): Coordinator = {
      componentManager.registerComponentType(compType)
      this
    }
    override def addEntityComponent(entity: Entity, component: Component): Coordinator = {
      this.synchronized(systemManager.entitySignatureChanged(entity, componentManager
        .bindComponentToEntity(entity, component)))
      this
    }
    override def addEntityComponents(entity: Entity,
                                     components: Component*): Coordinator = {
      components.foreach(this.addEntityComponent(entity, _))
      this
    }

    override def removeEntityComponent(entity: Entity, component: Component): Coordinator = {
      this.synchronized(systemManager.entitySignatureChanged(entity, componentManager
        .unbindComponentFromEntity(entity, component)))
      this
    }
    override def getEntityComponent[T: ClassTag](entity: Entity): T =
      this.synchronized(componentManager.getEntityComponent[T](entity))
    override def registerSystem(sys: System): Coordinator = {
      this.synchronized(systemManager.registerSystem(sys))
      this
    }
    override def updateSystems(): Unit = this.synchronized(systemManager.updateAll())
  }
  type ComponentType = Class[_]
  def apply(): Coordinator = new CoordinatorImpl()
}
