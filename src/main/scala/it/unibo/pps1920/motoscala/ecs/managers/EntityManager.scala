package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.Entity

private[managers] trait EntityManager {
  def addEntity(entity: Entity): Unit
  def removeEntity(entity: Entity): Unit
  def entities: Set[Entity]
}
private[managers] object EntityManager {
  private class EntityManagerImpl extends EntityManager {
    private var _entities: Set[Entity] = Set()

    override def addEntity(entity: Entity): Unit = _entities += entity
    override def removeEntity(entity: Entity): Unit = _entities = _entities filter (_ != entity)
    override def entities: Set[Entity] = _entities
  }
  def apply(): EntityManager = new EntityManagerImpl()
}