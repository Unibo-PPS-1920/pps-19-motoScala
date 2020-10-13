package it.unibo.pps1920.motoscala.ecs.systems

import java.net.URL

import alice.tuprolog.{Prolog, Struct, Theory, Var}
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
  def apply(coordinator: Coordinator, queue: CommandQueue): System = new AISystemImpl(coordinator, queue)
  private class AISystemImpl(coordinator: Coordinator, queue: CommandQueue)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent], classOf[AIComponent])) {

    val position = "/prolog/movement.pl"

    val engine = new Prolog()
    engine.setTheory(new Theory(new URL(loadFromJar(position)).openStream()))

    var frames = 0
    def update(): Unit = {
      frames = frames + 1
      if (frames % 1 == 0) {
        entitiesRef().foreach(e => {
          val pos = coordinator.getEntityComponent(e, classOf[PositionComponent]).get.asInstanceOf[PositionComponent]
            .pos
          val ai = coordinator.getEntityComponent(e, classOf[AIComponent]).get.asInstanceOf[AIComponent]
          val tPos = coordinator.getEntityComponent(BumperCarEntity(ai.target), classOf[PositionComponent]).get
            .asInstanceOf[PositionComponent].pos
          val s = engine.solve(new Struct("move2", (pos.x, pos.y).toString(), (tPos.x, tPos.y)
            .toString(), new Var(), new Var()))
          val t = s.getSolution
          val v = Vector2(x = extractTerm(t, 2), y = extractTerm(t, 3))
          queue
            .enqueue(CommandEvent(EventData.CommandData(e, Direction(v))))
        })
      }

    }

  }
}
