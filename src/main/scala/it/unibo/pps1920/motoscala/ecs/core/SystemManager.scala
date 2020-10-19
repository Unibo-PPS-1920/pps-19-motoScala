package it.unibo.pps1920.motoscala.ecs.core

import it.unibo.pps1920.motoscala.ecs.{Entity, System}

private[core] trait SystemManager {
  def registerSystem(sys: System): Unit
  def entitySignatureChanged(entity: Entity, enSignature: ECSSignature): Set[System]
  def entityDestroyed(entity: Entity): Unit
  def updateAll(): Unit
}

private[core] object SystemManager {
  private class SystemManagerImpl() extends SystemManager {
    private var systemSignature: Map[System, ECSSignature] = Map()
    private var systemList: List[System] = List()

    override def registerSystem(sys: System): Unit = {
      systemSignature += (sys -> sys.signature)
      systemList = sys :: systemList
    }
    override def entitySignatureChanged(entity: Entity, enSignature: ECSSignature): Set[System] = {
      val partition = systemSignature.partition(s => s._2.signatureSet.&(enSignature.signatureSet)
        == s._2.signatureSet)
      partition._1.keys.foreach(_.addEntityRef(entity))
      partition._2.keys.foreach(_.removeEntityRef(entity))
      partition._1.keySet
    }
    override def entityDestroyed(entity: Entity): Unit = systemSignature.keys.foreach(_.removeEntityRef(entity))
    override def updateAll(): Unit = systemList.foreach(_.update())
  }
  def apply(): SystemManager = new SystemManagerImpl()
}