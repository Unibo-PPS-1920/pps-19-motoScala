package it.unibo.pps1920.motoscala.ecs.components

import it.unibo.pps1920.motoscala.ecs.Component


case class DirectionComponent(var dir: Direction) extends Component {

}

object Direction {

  sealed trait Direction

  case object Running extends Direction

  case object Paused extends Direction

  case object Stopped extends Direction

}
