package it.unibo.pps1920.motoscala.ecs.core

import it.unibo.pps1920.motoscala.ecs.core.Coordinator.ComponentType

/** Represents an ECS Signature. It is a collection of components */
trait ECSSignature {

  /** Add the component or components to the signature so sign the component/s.
   *
   * @param componentType the component type
   * @return the signature
   */
  def signComponent(componentType: ComponentType*): ECSSignature

  /** Add the collection of components to the signature so sign the components.
   *
   * @param componentTypes the collection of components
   * @return the signature
   */
  def signComponent(componentTypes: Iterable[ComponentType]): ECSSignature

  /** Remove the component or components to the signature so repudiate the component/s.
   *
   * @param componentType the component type
   * @return the signature
   */
  def repudiateComponent(componentType: ComponentType*): ECSSignature

  /** Remove the collection of components to the signature so repudiate the components.
   *
   * @param componentTypes the collection of components
   * @return the signature
   */
  def repudiateComponent(componentTypes: Iterable[ComponentType]): ECSSignature

  /** Get the set of component that compose the signature.
   *
   * @return
   */
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

  /** Factory for [[ECSSignature]] instances */
  def apply(types: Iterable[ComponentType]): ECSSignature = {
    ECSSignatureImpl().signComponent(types)
  }

  /** Factory for [[ECSSignature]] instances */
  def apply(componentType: ComponentType*): ECSSignature = {
    apply(componentType)
  }
}