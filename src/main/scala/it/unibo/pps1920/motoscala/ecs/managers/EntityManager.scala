package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.Entity
import it.unibo.pps1920.motoscala.ecs.managers.Coordinator.EntitySignature

trait EntityManager {
  def createEntity(entity: Entity): Unit
  def removeEntity(entity: Entity): Unit
  def entities: Set[Entity]
  def getEntitySignature(entity: Entity): Option[EntitySignature]
  def signEntity(entity: Entity, signature: EntitySignature): Unit
}
object EntityManager {
  private class EntityManagerImpl extends EntityManager {
    private var _entities: Set[Entity] = Set()
    private var signatures: Map[Entity, EntitySignature] = Map()

    override def createEntity(entity: Entity): Unit = _entities = _entities + entity
    override def removeEntity(entity: Entity): Unit = _entities = _entities filter (_ != entity)
    override def entities: Set[Entity] = _entities
    override def getEntitySignature(entity: Entity): Option[EntitySignature] = signatures get entity
    override def signEntity(entity: Entity, signature: EntitySignature): Unit =
      signatures = signatures + (entity -> signature)
  }
  def apply(): EntityManager = new EntityManagerImpl()
}