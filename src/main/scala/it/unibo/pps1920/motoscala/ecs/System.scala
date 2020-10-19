package it.unibo.pps1920.motoscala.ecs

import it.unibo.pps1920.motoscala.ecs.core.ECSSignature
import org.slf4j.{Logger, LoggerFactory}

trait System {
  def signature: ECSSignature
  def entitiesRef(): Set[Entity]
  def addEntityRef(entity: Entity): Unit
  def removeEntityRef(entity: Entity): Unit
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
