package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.managers.Coordinator.ComponentType
import it.unibo.pps1920.motoscala.ecs.{Component, Entity, System}

trait Coordinator {
  def createEntity(entity: Entity): Unit
  def removeEntity(entity: Entity): Unit
  def registerComponentType(compType: ComponentType): Unit
  def addEntityComponent(entity: Entity, component: Component): Unit
  def removeEntityComponent(entity: Entity, component: Component): Unit
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

    import ImplicitConversions._
    override def createEntity(entity: Entity): Unit = entityManager createEntity entity
    override def removeEntity(entity: Entity): Unit = {
      entityManager removeEntity entity
      systemManager entityDestroyed entity
    }
    override def registerComponentType(compType: ComponentType): Unit = componentManager.registerComponentType(compType)
    override def addEntityComponent(entity: Entity, component: Component): Unit = {
      val signatureByEManger = entityManager.updateSignature(entity, _.signComponent(component))
      val signatureByCManager = componentManager.bindComponentToEntity(entity, component)
      require(signatureByEManger == signatureByCManager)
      systemManager.entitySignatureChanged(entity, signatureByEManger)
    }
    override def removeEntityComponent(entity: Entity, component: Component): Unit = {
      val signatureByEManger = entityManager.updateSignature(entity, _.repudiateComponent(component))
      val signatureByCManager = componentManager.unbindComponentFromEntity(entity, component)
      require(signatureByEManger == signatureByCManager)
      systemManager.entitySignatureChanged(entity, signatureByEManger)
    }
    override def getEntityComponent(entity: Entity, compType: ComponentType): Option[Component] =
      componentManager.getEntityComponent(entity, compType)
    override def registerSystem(sys: System): Unit = systemManager.registerSystem(sys)
    override def signSystem(sys: System, sysSignature: ECSSignature): Unit = systemManager
      .setSignature(sys, sysSignature)
    override def updateSystems(): Unit = systemManager.updateAll()

    object ImplicitConversions {
      implicit def componentToComponentType(component: Component): ComponentType = component.getClass
    }
  }
  type ComponentType = Class[_]
  def apply(): Coordinator = new CoordinatorImpl()
}
