package it.unibo.pps1920.motoscala.ecs.core

import it.unibo.pps1920.motoscala.ecs.core.Coordinator.ComponentType

trait ECSSignature {
  def signComponent(componentType: ComponentType*): ECSSignature
  def signComponent(componentTypes: Iterable[ComponentType]): ECSSignature
  def repudiateComponent(componentType: ComponentType*): ECSSignature
  def repudiateComponent(componentTypes: Iterable[ComponentType]): ECSSignature
  def signatureSet: Set[ComponentType]
}
object ECSSignature {
  private case class ECSSignatureImpl() extends ECSSignature {
    private var signature: Set[ComponentType] = Set()

    override def signComponent(componentType: ComponentType*): ECSSignature = {
      signature = signature ++ componentType
      this
    }
    override def signComponent(componentTypes: Iterable[ComponentType]): ECSSignature = {
      componentTypes.foreach(signComponent(_))
      this
    }
    override def repudiateComponent(componentType: ComponentType*): ECSSignature = {
      signature = signature -- componentType
      this
    }
    override def signatureSet: Set[ComponentType] = signature
    override def repudiateComponent(componentTypes: Iterable[ComponentType]): ECSSignature = {
      componentTypes.foreach(signComponent(_))
      this
    }
  }
  def apply(types: Iterable[ComponentType]): ECSSignature = {
    val instance = ECSSignatureImpl()
    instance.signComponent(types)
  }
  def apply(componentType: ComponentType*): ECSSignature = {
    apply(componentType)
  }
}