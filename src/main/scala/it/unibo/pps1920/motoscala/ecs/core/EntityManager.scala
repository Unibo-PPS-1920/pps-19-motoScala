package it.unibo.pps1920.motoscala.ecs.core

import it.unibo.pps1920.motoscala.ecs.Entity

/** An Entity Manager manages [[it.unibo.pps1920.motoscala.ecs.Entity]].
 * <p>
 * It allows entity addition: like creation in ECS framework.
 * <p>
 * It allows entity removal: like deletion in ECS framework.
 * */
protected[core] trait EntityManager {
  /** Add the entity to the manager.
   *
   * @param entity the entity
   */
  def addEntity(entity: Entity): Unit
  /** Remove the entity to the manager.
   *
   * @param entity the entity
   */
  def removeEntity(entity: Entity): Unit
  /** Get all entities managed.
   */
  def entities: Set[Entity]
}

protected[core] object EntityManager {
  private class EntityManagerImpl extends EntityManager {
    private var _entities: Set[Entity] = Set()

    override def addEntity(entity: Entity): Unit = _entities += entity
    override def removeEntity(entity: Entity): Unit = _entities = _entities filter (_ != entity)
    override def entities: Set[Entity] = _entities
  }

  /** Factory for [[EntityManager]] instances */
  def apply(): EntityManager = new EntityManagerImpl()
}