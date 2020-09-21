package it.unibo.pps1920.motoscala.ecs.managers

import it.unibo.pps1920.motoscala.ecs.{Entity, System}

trait SystemManager {
  def registerSystem(sys: System): Unit
  def setSignature(sys: System, sysSignature: ECSSignature): Unit
  def entitySignatureChanged(entity: Entity, enSignature: ECSSignature): Set[System]
  def entityDestroyed(entity: Entity): Unit
  def updateAll(): Unit
}

object SystemManager {
  private class SystemManagerImpl() extends SystemManager {
    private var systemSignature: Map[System, ECSSignature] = Map()

    override def registerSystem(sys: System): Unit = systemSignature += (sys -> null)
    override def setSignature(sys: System, sysSignature: ECSSignature): Unit = systemSignature += (sys -> sysSignature)
    override def entitySignatureChanged(entity: Entity, enSignature: ECSSignature): Set[System] = {
      val partition = systemSignature.partition(_._2 == enSignature)
      partition._1.keys.foreach(s => s.addEntityRef(entity))
      partition._2.keys.foreach(s => s.removeEntityRef(entity))
      partition._1.keySet
    }
    override def entityDestroyed(entity: Entity): Unit = systemSignature.keys.foreach(_.removeEntityRef(entity))
    override def updateAll(): Unit = systemSignature.keys.foreach(_.update())
  }
  def apply(): SystemManager = new SystemManagerImpl()
}