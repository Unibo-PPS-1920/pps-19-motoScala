package it.unibo.pps1920.motoscala.ecs.core

import it.unibo.pps1920.motoscala.ecs.core.Coordinator.ComponentType
import it.unibo.pps1920.motoscala.ecs.{Component, Entity}

import scala.reflect.ClassTag

/** A component manager manages [[it.unibo.pps1920.motoscala.ecs.Component]].
 * <p>
 * It allows component registration, binding to entity, and retrieving.
 * <p>
 * The set of component connected to an entity define an [[it.unibo.pps1920.motoscala.ecs.core.ECSSignature]]
 * */
protected[core] trait ComponentManager {
  /** Register a component to the manager so that it can be bound to entities.
   * <p>
   * An unregistered component, if used, will produce an IllegalArgumentException.
   *
   * @param compType the component type
   */
  def registerComponentType(compType: ComponentType): Unit
  /** Bind the component the entity.
   * <p>
   * This will update che effective Signature of the component removing it.
   *
   * @param entity the entity
   * @param component the component to bind
   * @return the signature
   */
  def bindComponentToEntity(entity: Entity, component: Component): ECSSignature
  /** Unbind the component the entity.
   * <p>
   * This will update che effective Signature of the component removing it.
   *
   * @param entity the entity
   * @param component the component to unbind
   * @return the signature
   */
  def unbindComponentFromEntity(entity: Entity, component: Component): ECSSignature
  /** Get the entity signature.
   * <p>
   * It is indeed the set of component bound to the entity.
   *
   * @param entity the entity
   * @return the signature
   */
  def getEntitySignature(entity: Entity): ECSSignature
  /** Get the component bound to the given entity.
   * <p>
   *
   * @tparam T the component type
   * @param entity the entity bound to component
   * @return the casted component
   */
  def getEntityComponent[T: ClassTag](entity: Entity): T
  /** Destroys the entity.
   * <p>
   * It is called from other modules
   *
   * @param entity the entity to delete
   */
  def entityDestroyed(entity: Entity): Unit
}

protected[core] object ComponentManager {
  private class ComponentManagerImpl extends ComponentManager {
    private var registered: Set[ComponentType] = Set()
    private var entityComponent: Map[Entity, Map[ComponentType, Component]] = Map()

    import ImplicitConversions._
    import MagicValues._

    private def checkRegisteredComponent(compType: ComponentType): Boolean = registered contains compType

    override def registerComponentType(compType: ComponentType): Unit = registered = registered + compType

    override def bindComponentToEntity(entity: Entity, component: Component): ECSSignature = {
      require(checkRegisteredComponent(component), RequireMessage)
      entityComponent = entityComponent.updatedWith(entity)({
        case None => Option(Map((component, component)))
        case Some(map) => Option(map + ((component, component)))
      })
      getEntitySignature(entity)
    }

    override def unbindComponentFromEntity(entity: Entity, component: Component): ECSSignature = {
      require(checkRegisteredComponent(component), RequireMessage)
      entityComponent = entityComponent.updatedWith(entity)({
        case None => Option(Map())
        case Some(map) => Option(map - component)
      })
      getEntitySignature(entity)
    }

    override def getEntityComponent[T: ClassTag](entity: Entity): T =
      entityComponent.get(entity).flatMap(_.get(implicitly[ClassTag[T]].runtimeClass)).get.asInstanceOf[T]

    override def entityDestroyed(entity: Entity): Unit = entityComponent -= entity

    override def getEntitySignature(entity: Entity): ECSSignature = ECSSignature(entityComponent(entity).keySet)


    private object ImplicitConversions {
      implicit def componentToComponentType(component: Component): ComponentType = component.getClass
    }

    private object MagicValues {
      val RequireMessage = "Cannot use an unregistered component"
    }
  }

  /** Factory for [[ComponentManager]] instances */
  def apply(): ComponentManager = new ComponentManagerImpl()
}