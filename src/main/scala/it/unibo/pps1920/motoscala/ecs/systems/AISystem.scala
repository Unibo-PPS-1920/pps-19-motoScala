package it.unibo.pps1920.motoscala.ecs.systems

import java.net.URL

import alice.tuprolog.{Struct, Term, Theory, Var}
import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.loadFromJar
import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData
import it.unibo.pps1920.motoscala.ecs.components.{AIComponent, PositionComponent}
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Scala2P._
import it.unibo.pps1920.motoscala.ecs.util.{Direction, Vector2}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import it.unibo.pps1920.motoscala.engine.CommandQueue

import scala.language.postfixOps

object AISystem {
  def apply(coordinator: Coordinator, queue: CommandQueue): System = new InputSystemImpl(coordinator, queue)
  private class InputSystemImpl(coordinator: Coordinator, queue: CommandQueue)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[AIComponent])) {

    val position = "/prolog/movement.pl"

    val engine: Term => LazyList[Term] = mkPrologEngine(new Theory(new URL(loadFromJar(position))
                                                                     .openStream()))

    def update(): Unit = {
      entitiesRef().foreach(e => {
        val pos = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent].pos
        val ai = coordinator.getEntityComponent(e, classOf[AIComponent]).get.asInstanceOf[AIComponent]
        val tPos = coordinator.getEntityComponent(BumperCarEntity(ai.target), classOf[PositionComponent]).get
          .asInstanceOf[PositionComponent].pos

        val in = new Struct("move", (pos.x, pos.y).toString(), (tPos.x, tPos.y).toString(), new Var())
        val t = extractTerm(engine(in).head, 2)
        queue
          .enqueue(CommandEvent(EventData.CommandData(e, Direction(Vector2(t._1.doubleValue(), t._2.doubleValue())))))
      })
    }
  }
}
