package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.Entity

private[managers] trait EntityManager {
  def createEntity(entity: Entity): Unit
  def removeEntity(entity: Entity): Unit
  def entities: Set[Entity]
  def getEntitySignature(entity: Entity): Option[ECSSignature]
  def signEntity(entity: Entity, signature: ECSSignature): ECSSignature
  def updateSignature(entity: Entity, sign: ECSSignature => ECSSignature): ECSSignature
}
private[managers] object EntityManager {
  private class EntityManagerImpl extends EntityManager {
    private var _entities: Set[Entity] = Set()
    private var signatures: Map[Entity, ECSSignature] = Map()

    override def createEntity(entity: Entity): Unit = _entities += entity
    override def removeEntity(entity: Entity): Unit = _entities = _entities filter (_ != entity)
    override def entities: Set[Entity] = _entities
    override def getEntitySignature(entity: Entity): Option[ECSSignature] = signatures get entity
    override def signEntity(entity: Entity, signature: ECSSignature): ECSSignature = {
      signatures += (entity -> signature)
      signature
    }
    override def updateSignature(entity: Entity, map: ECSSignature => ECSSignature): ECSSignature =
      signatures.get(entity).fold[ECSSignature](ECSSignature())(signature => signEntity(entity, map(signature)))
  }
  def apply(): EntityManager = new EntityManagerImpl()
}