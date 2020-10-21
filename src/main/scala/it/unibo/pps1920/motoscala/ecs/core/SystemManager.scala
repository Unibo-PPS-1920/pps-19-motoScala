package it.unibo.pps1920.motoscala.ecs.core

import it.unibo.pps1920.motoscala.ecs.{Entity, System}

/** A system manager manages [[it.unibo.pps1920.motoscala.ecs.System]].
 * <p>
 * It allows system registration.
 * <p>
 * It allows system redistribution of entities according to their signature.
 * */
protected[core] trait SystemManager {
  /** Register the system to the manager
   *
   * @param system the system
   */
  def registerSystem(system: System): Unit

  /** A change in the entity signature makes the manager redistribute systems according to entity signature.
   *
   * @param entity the entity
   * @param enSignature the entity signature
   * @return the set of systems
   */
  def entitySignatureChanged(entity: Entity, enSignature: ECSSignature): Set[System]

  /** Remove an entity from all system who's signature is a subset of the entity signature.
   *
   * @param entity the entity to remove
   */
  def entityDestroyed(entity: Entity): Unit

  /**
   * Update all systems
   */
  def updateAll(): Unit
}

protected[core] object SystemManager {
  private class SystemManagerImpl extends SystemManager {
    private var systemSignature: Map[System, ECSSignature] = Map()
    private var systemList: List[System] = List()

    override def registerSystem(system: System): Unit = {
      systemSignature += (system -> system.signature)
      systemList = system :: systemList
    }

    override def entitySignatureChanged(entity: Entity, enSignature: ECSSignature): Set[System] = {
      val partition = systemSignature.partition(s => s._2.signatureSet.&(enSignature.signatureSet) == s._2.signatureSet)
      partition._1.keys.foreach(_.addEntityRef(entity))
      partition._2.keys.foreach(_.removeEntityRef(entity))
      partition._1.keySet
    }

    override def entityDestroyed(entity: Entity): Unit = systemSignature.keys.foreach(_.removeEntityRef(entity))

    override def updateAll(): Unit = systemList.foreach(_.update())
  }

  /** Factory for [[SystemManager]] instances */
  def apply(): SystemManager = new SystemManagerImpl()
}