package it.unibo.pps1920.motoscala.ecs.managers.errors

trait ManagerError
object ManagerError {
  final case class UnregisteredComponentError() extends ManagerError
  final case class EntityNotFoundError() extends ManagerError
  final case class OtherError(ex: Exception) extends ManagerError
}
