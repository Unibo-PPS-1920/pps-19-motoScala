package it.unibo.pps1920.motoscala.ecs.systems

import java.net.URL

import alice.tuprolog._
import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.loadFromJarToString
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
  private class AISystemImpl(coordinator: Coordinator, queue: CommandQueue, skipFrames: scala.Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[AIComponent])) {

    private val position = "/prolog/movement.pl"
    private val engine = new Prolog()
    engine.setTheory(new Theory(new URL(loadFromJarToString(position)).openStream()))

    private var frames = 0
    private val X = 4
    private val Y = 5
    private val idlePosition = (0, 0)

    def update(): Unit = {
      frames = frames + 1
      if (frames % skipFrames == 0) {
        entitiesRef().foreach(f = e => {
          val positions = entitiesRef().filter(_.uuid != e.uuid).map(e2 => {
            val p = coordinator.getEntityComponent[PositionComponent](e2).pos
            (p.x, p.y)
          })
          val pos = coordinator.getEntityComponent[PositionComponent](e).pos
          val ai = coordinator.getEntityComponent[AIComponent](e)

          ai.targets.popWhile(!coordinator.isEntityAlive(_))

          val tPos: Vector2 = if (ai.targets.nonEmpty) {
            coordinator.getEntityComponent[PositionComponent](ai.targets.head).pos
          } else {
            idlePosition
          }
          val query = new Struct("move_avoiding", ai.foolishness,
                                 (pos.x, pos.y),
                                 (tPos.x, tPos.y),
                                 positions,
                                 new Var(),
                                 new Var())
          val s = engine.solve(query).getSolution

          val v = Vector2(x = extractTerm(s, X), y = extractTerm(s, Y))
          queue
            .enqueue(CommandEvent(EventData.CommandData(e, Direction(v))))
        })
      }

    }

  }
}
