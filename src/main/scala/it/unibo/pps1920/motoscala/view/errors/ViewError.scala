package it.unibo.pps1920.motoscala.view.errors

private[view] trait ViewError
private[view] object ViewError {
  final case class ScreenNotfound() extends ViewError
  final case class OtherError(ex: Exception) extends ViewError
}
