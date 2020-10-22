package it.unibo.pps1920.motoscala.ecs

import it.unibo.pps1920.motoscala.ecs.core.ECSSignature
import org.slf4j.{Logger, LoggerFactory}

/** Represents a System in ECS framework */
trait System {
  /** Get the system signature.
   *
   * @return the signature
   */
  def signature: ECSSignature

  /** Get the entities managed by the system.
   *
   * @return the set of entities
   */
  def entitiesRef(): Set[Entity]

  /** Add the entity reference to the system.
   *
   * @param entity the entity to add
   */
  def addEntityRef(entity: Entity): Unit

  /** Remove the entity reference from the system.
   *
   * @param entity the entity to remove
   */
  def removeEntityRef(entity: Entity): Unit

  /** Update the system
   */
  def update(): Unit
}

abstract class AbstractSystem(override val signature: ECSSignature) extends System {
  private var _entitiesRef: Set[Entity] = Set()
  protected val logger: Logger = LoggerFactory getLogger this.getClass

  override def entitiesRef(): Set[Entity] = _entitiesRef
  override def addEntityRef(entity: Entity): Unit = _entitiesRef += entity
  override def removeEntityRef(entity: Entity): Unit = _entitiesRef -= entity
  override def update(): Unit
}
