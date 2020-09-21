package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator.ECSSignature

trait EntityManager {
  def createEntity(entity: Entity): Unit
  def removeEntity(entity: Entity): Unit
  def entities: Set[Entity]
  def getEntitySignature(entity: Entity): Option[ECSSignature]
  def signEntity(entity: Entity, signature: ECSSignature): Unit
}
object EntityManager {
  private class EntityManagerImpl extends EntityManager {
    private var _entities: Set[Entity] = Set()
    private var signatures: Map[Entity, ECSSignature] = Map()

    override def createEntity(entity: Entity): Unit = _entities = _entities + entity
    override def removeEntity(entity: Entity): Unit = _entities = _entities filter (_ != entity)
    override def entities: Set[Entity] = _entities
    override def getEntitySignature(entity: Entity): Option[ECSSignature] = signatures get entity
    override def signEntity(entity: Entity, signature: ECSSignature): Unit =
      signatures = signatures + (entity -> signature)
  }
  def apply(): EntityManager = new EntityManagerImpl()
}