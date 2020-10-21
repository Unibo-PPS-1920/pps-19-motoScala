package it.unibo.pps1920.motoscala.ecs.core

import it.unibo.pps1920.motoscala.ecs.core.Coordinator.ComponentType
import it.unibo.pps1920.motoscala.ecs.{Component, Entity, System}

import scala.reflect.ClassTag

/** A coordinator coordinate managers.
 * <p>
 * It controls all logic and ensure safety in interactions between managers
 * */
trait Coordinator {
  /** Add the entity to the manager.
   *
   * @param entity the entity
   */
  def addEntity(entity: Entity): Coordinator

  /** Remove the entity to the manager.
   *
   * @param entity the entity
   */
  def removeEntity(entity: Entity): Coordinator

  /** Check if an entity is present a.k.a alive in ECS.
   *
   * @param entity the entity
   */
  def isEntityAlive(entity: Entity): Boolean

  /** Register a component to the manager so that it can be bound to entities.
   * <p>
   * An unregistered component, if used, will produce an IllegalArgumentException.
   *
   * @param compType the component type
   */
  def registerComponentType(compType: ComponentType): Coordinator

  /** Bind the component to the entity.
   * <p>
   * This will update che effective Signature of the component removing it.
   *
   * @param entity the entity
   * @param component the component to bind
   * @return the signature
   */
  def addEntityComponent(entity: Entity, component: Component): Coordinator

  /** Bind the components to the entity.
   * <p>
   * This will update che effective Signature of the component removing it.
   *
   * @param entity the entity
   * @param components the components to bind
   * @return the signature
   */
  def addEntityComponents(entity: Entity, components: Component*): Coordinator

  /** Unbind the component from the entity.
   * <p>
   * This will update che effective Signature of the component removing it.
   *
   * @param entity the entity
   * @param component the component to unbind
   * @return the signature
   */
  def removeEntityComponent(entity: Entity, component: Component): Coordinator

  /** Get the component that is bound to the given entity.
   * <p>
   *
   * @tparam T the component type
   * @param entity the entity bound to component
   * @return the casted component
   */
  def getEntityComponent[T: ClassTag](entity: Entity): T

  /** Register the system to the manager
   *
   * @param system the system
   */
  def registerSystem(system: System): Coordinator

  /**
   * Update all systems
   */
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

    override def addEntityComponents(entity: Entity, components: Component*): Coordinator = {
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

    override def registerSystem(system: System): Coordinator = {
      this.synchronized(systemManager.registerSystem(system))
      this
    }

    override def updateSystems(): Unit = this.synchronized(systemManager.updateAll())
  }
  type ComponentType = Class[_]

  /** Factory for [[Coordinator]] instances */
  def apply(): Coordinator = new CoordinatorImpl()
}
