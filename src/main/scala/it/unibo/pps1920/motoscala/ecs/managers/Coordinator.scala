package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.{Component, Entity, System}

import scala.util.Try

trait Coordinator {
  import it.unibo.pps1920.motoscala.ecs.managers.Coordinator.ComponentType
  def createEntity(entity: Entity): Unit
  def removeEntity(entity: Entity): Unit
  def registerComponentType(compType: ComponentType): Unit
  def addEntityComponent(entity: Entity, component: Component): Try[(Entity, Component)]
  def removeEntityComponent(entity: Entity, component: Component): Try[(Entity, Component)]
  def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component]
  def registerSystem(sys: System): Unit
  def signSystem(sys: System, sysSignature: ECSSignature): Unit
  def updateSystems(): Unit
}

object Coordinator {
  private class CoordinatorImpl() extends Coordinator {
    private val componentManager = ComponentManager()
    private val systemManager = SystemManager()
    private val entityManager = EntityManager()

    override def createEntity(entity: Entity): Unit = entityManager.createEntity(entity)
    override def removeEntity(entity: Entity): Unit = entityManager.removeEntity(entity)
    override def registerComponentType(compType: ComponentType): Unit = componentManager.registerComponentType(compType)
    override def addEntityComponent(entity: Entity, component: Component): Try[(Entity, Component)] = ???
    override def removeEntityComponent(entity: Entity, component: Component): Try[(Entity, Component)] = {
      componentManager.unbindComponentFromEntity(entity, component)
    }
    override def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component] =
      componentManager.getEntityComponent(entity, compType)
    override def registerSystem(sys: System): Unit = systemManager.registerSystem(sys)
    override def signSystem(sys: System, sysSignature: ECSSignature): Unit = systemManager
      .setSignature(sys, sysSignature)
    override def updateSystems(): Unit = systemManager.updateAll()
  }
  type ComponentType = Class[_]
  def apply(): Coordinator = new CoordinatorImpl()
}
