package it.unibo.pps1920.motoscala.ecs.systems

import java.net.URL

import alice.tuprolog._
import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.loadFromJar
import it.unibo.pps1920.motoscala.controller.mediation.Event.CommandEvent
import it.unibo.pps1920.motoscala.controller.mediation.EventData
import it.unibo.pps1920.motoscala.ecs.components.{AIComponent, PositionComponent}
import it.unibo.pps1920.motoscala.ecs.core.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Scala2P._
import it.unibo.pps1920.motoscala.ecs.util.{Direction, Vector2}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, System}
import it.unibo.pps1920.motoscala.engine.CommandQueue

import scala.language.postfixOps

object AISystem {
  def apply(coordinator: Coordinator, queue: CommandQueue,
            skipFrames: scala.Int): System = new AISystemImpl(coordinator, queue, skipFrames)
  private class AISystemImpl(coordinator: Coordinator, queue: CommandQueue, skipframes: scala.Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[AIComponent])) {

    val position = "/prolog/movement.pl"

    val engine = new Prolog()
    engine.setTheory(new Theory(new URL(loadFromJar(position)).openStream()))

    var frames = 0

    def update(): Unit = {
      frames = frames + 1
      if (frames % skipframes == 0) {
        entitiesRef().foreach(f = e => {
          val positions = entitiesRef().filter(_.uuid != e.uuid).map(e2 => {
            val p = coordinator.getEntityComponent[PositionComponent](e2).pos
            (p.x, p.y)
          }).toSeq
          val pos = coordinator.getEntityComponent[PositionComponent](e).pos
          val ai = coordinator.getEntityComponent[AIComponent](e)
          ai.targets.popWhile(!coordinator.isEntityAlive(_))
          val tPos: Vector2 = if (ai.targets.nonEmpty) {
            coordinator.getEntityComponent[PositionComponent](ai.targets.head).pos
          } else {
            (0, 0)
          }
          val query = new Struct("move_avoiding", ai.foolishness,
                                 (pos.x, pos.y),
                                 (tPos.x, tPos.y),
                                 positions,
                                 new Var(),
                                 new Var())
          val s = engine.solve(query).getSolution
          val v = Vector2(x = extractTerm(s, 4), y = extractTerm(s, 5))
          queue
            .enqueue(CommandEvent(EventData.CommandData(e, Direction(v))))
        })
      }

    }

  }
}
