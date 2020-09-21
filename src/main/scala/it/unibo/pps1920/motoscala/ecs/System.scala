package it.unibo.pps1920.motoscala.ecs

trait System {
  def entitiesRef(): Set[Entity]
  def addEntityRef(entity: Entity): Unit
  def removeEntityRef(entity: Entity): Unit
  def update(): Unit
}

abstract class AbstractSystem() extends System {
  private var _entitiesRef: Set[Entity] = Set()

  override def entitiesRef(): Set[Entity] = _entitiesRef
  override def addEntityRef(entity: Entity): Unit = _entitiesRef += entity
  override def removeEntityRef(entity: Entity): Unit = _entitiesRef -= entity
  def update(): Unit
}
