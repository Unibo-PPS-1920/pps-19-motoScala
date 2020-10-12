package it.unibo.pps1920.motoscala.ecs.systems

import java.net.URL

import alice.tuprolog.{Struct, Term, Theory, Var}
import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.loadFromJar
import it.unibo.pps1920.motoscala.ecs.components.{DirectionComponent, PositionComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Scala2P._
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}

object AISystem {
  def apply(coordinator: Coordinator): System = new InputSystemImpl(coordinator)
  private class InputSystemImpl(coordinator: Coordinator)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[DirectionComponent])) {

    val position = "/prolog/movement.pl"

    val engine: Term => LazyList[Term] = mkPrologEngine(new Theory(new URL(loadFromJar(position))
                                                                     .openStream()))

    def update(): Unit = {
      val in = new Struct("move", "(9,0)", "(0,0)", new Var())
      val path = engine(in) map (extractTerm(_, 2)) take 1 foreach (print(_))
    }
  }
}
