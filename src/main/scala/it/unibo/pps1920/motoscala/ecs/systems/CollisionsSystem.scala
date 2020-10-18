package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.controller.mediation.Event.RedirectSoundEvent
import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}
import it.unibo.pps1920.motoscala.ecs.components.{CollisionComponent, PositionComponent, ShapeComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Component, Entity, System}
object CollisionsSystem {

  def apply(coordinator: Coordinator, controller: EngineController,
            fps: Int): System = new CollisionsSystemImpl(coordinator, controller, fps)

  private class CollisionsSystemImpl(coordinator: Coordinator, controller: EngineController, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent],
                                        classOf[ShapeComponent],
                                        classOf[VelocityComponent],
                                        classOf[CollisionComponent])) {

    private val CollisionDuration = fps / 6
    //not used because we chose to keep the realistic collision velocity until the end of the collision
    private val CollisionVelocity = Vector2(30, 30)

    private var positionCompE1: PositionComponent = _
    private var shapeCompE1: ShapeComponent = _
    private var velocityCompE1: VelocityComponent = _
    private var collisionCompE1: CollisionComponent = _

    private var positionCompE2: PositionComponent = _
    private var shapeCompE2: ShapeComponent = _
    private var velocityCompE2: VelocityComponent = _
    private var collisionCompE2: CollisionComponent = _

    override def update(): Unit = {
      var entitiesToCheck = entitiesRef()
      entitiesRef().foreach(e1 => {
        entitiesToCheck -= e1
        collisionCompE1 = extractComponent[CollisionComponent](e1, classOf[CollisionComponent])
        velocityCompE1 = extractComponent[VelocityComponent](e1, classOf[VelocityComponent])
        if (collisionCompE1.isColliding) {
          collisionStep(collisionCompE1, velocityCompE1)

        } else {
          velocityCompE1.currentVel = velocityCompE1.inputVel
        }
        entitiesToCheck.foreach(e2 => {
          collisionCompE2 = extractComponent[CollisionComponent](e2, classOf[CollisionComponent])
          velocityCompE2 = extractComponent[VelocityComponent](e2, classOf[VelocityComponent])
          shapeCompE1 = extractComponent[ShapeComponent](e1, classOf[ShapeComponent])
          shapeCompE2 = extractComponent[ShapeComponent](e2, classOf[ShapeComponent])
          positionCompE1 = extractComponent[PositionComponent](e1, classOf[PositionComponent])
          positionCompE2 = extractComponent[PositionComponent](e2, classOf[PositionComponent])
          checkCollision()
        })
      })
    }

    private def checkCollision(): Unit = {
      (shapeCompE1.shape, shapeCompE2.shape) match {
        case (Circle(e1Radius), Circle(e2Radius)) => if ((positionCompE1.pos dist positionCompE2
          .pos) <= (e1Radius + e2Radius)) {
          if (!(velocityCompE2.currentVel.isZero() && velocityCompE1.currentVel.isZero())) {
            collide()
            collisionStep(collisionCompE1, velocityCompE1)
            collisionStep(collisionCompE2, velocityCompE2)
          }
        }
        case (circle: Circle, rectangle: Rectangle) => velocityCompE1.currentVel = (velocityCompE1
          .currentVel mul getDirInversion(circle, positionCompE1.pos, rectangle, positionCompE2.pos))
        case (rectangle: Rectangle, circle: Circle) => velocityCompE2.currentVel = velocityCompE2
          .currentVel mul getDirInversion(circle, positionCompE2.pos, rectangle, positionCompE1.pos)
        case _ => logger warn s"unexpected shape collision: ${shapeCompE1.shape} and ${shapeCompE1.shape}"
      }
    }

    private def getDirInversion(circle: Circle, circlePos: Vector2, rectangle: Rectangle,
                                rectanglePos: Vector2): Vector2 = {
      val testEdge = circlePos
      val inversionVec = Vector2(1, 1)
      //find closest edge
      if (circlePos.x < rectanglePos.x) { //left edge
        testEdge.x = rectanglePos.x
        inversionVec.x = -1
      }
      else if (circlePos.x > rectanglePos.x + rectangle.dimX) { //right edge
        testEdge.x = rectanglePos.x + rectangle.dimX
        inversionVec.x = -1
      }

      if (circlePos.y < rectanglePos.y) { //top edge
        testEdge.y = rectanglePos.y
        inversionVec.y = -1
      }
      else if (circlePos.y > rectanglePos.y + rectangle.dimY) { //bottom edge
        testEdge.y = rectanglePos.y + rectangle.dimY
        inversionVec.y = -1
      }

      //check distance from closest edge
      if ((circlePos dist testEdge) <= circle.radius) inversionVec //collide: direction inverted
      else Vector2(1, 1) //do not collide: same direction

    }

    private def collide(): Unit = {
      controller.mediator.publishEvent(RedirectSoundEvent(PlaySoundEffect(Clips.Collision)))
      startCollision()

      /* COLLISION CORE */

      if (collisionCompE1.mass != 0 && collisionCompE2.mass != 0) {
        /*logger debug s"Before collision: velocity ent1: ${velocityCompE1.currentVel}  velocity ent2 = ${
          velocityCompE2.currentVel
        }"*/

        // Compute unit normal and unit tangent vectors
        val normalVector = positionCompE2.pos sub positionCompE1.pos
        val unitNormalVector = normalVector.unitVector()
        val unitTangentVector = Vector2(-unitNormalVector.y, unitNormalVector.x)

        // Compute scalar projections of velocities onto unitNormalVector and unitTangentVector
        val normProjection1 = unitNormalVector dot velocityCompE1.currentVel
        val tangProjection1 = unitTangentVector dot velocityCompE1.currentVel
        val normProjection2 = unitNormalVector dot velocityCompE2.currentVel
        val tangProjection2 = unitTangentVector dot velocityCompE2.currentVel

        // Compute new normal velocities using one-dimensional elastic collision equations in the normal direction
        val newNormProjection1 = computeCollVel(normProjection1, normProjection2, collisionCompE1
          .mass + 1, collisionCompE2.mass)
        val newNormProjection2 = computeCollVel(normProjection2, normProjection1, collisionCompE2.mass, collisionCompE1
          .mass + 1)

        // Compute new normal and tangential velocity vectors
        val newNorVec1 = unitNormalVector.dot(newNormProjection1)
        val newTanVec1 = unitTangentVector.dot(tangProjection1)
        val newNorVec2 = unitNormalVector.dot(newNormProjection2)
        val newTanVec2 = unitTangentVector.dot(tangProjection2)

        // Set new velocities in x and y coordinates
        velocityCompE1.currentVel = newNorVec1 add newTanVec1
        velocityCompE2.currentVel = newNorVec2 add newTanVec2
        /*logger debug s"Computed collision: velocity ent1: ${velocityCompE1.currentVel}  velocity ent2 = ${
          velocityCompE2.currentVel
        }"*/
      }
    }

    private def computeCollVel(vel1: Double, vel2: Double, mass1: Double, mass2: Double): Double =
      (vel1 * (mass1 - mass2) + 2 * mass2 * vel2) / (mass1 + mass2)

    private def startCollision(): Unit = {
      collisionCompE1.isColliding = true
      collisionCompE2.isColliding = true
      collisionCompE1.duration = CollisionDuration
      collisionCompE2.duration = CollisionDuration
      /*collisionCompE1.oldSpeed = velCompE1.vel.abs()
        collisionCompE2.oldSpeed = velCompE2.vel.abs()*/
      /*velCompE1.vel = CollisionVelocity mul CollisionVelocity.dir()
      velCompE2.vel = CollisionVelocity mul CollisionVelocity.dir()*/

    }
    private def extractComponent[T <: Component](e: Entity, componentClass: Predef.Class[_]): T = {
      coordinator.getEntityComponent(e, componentClass).get.asInstanceOf[T]
    }

    //Performs a collision step, decrementing the collision duration and handling termination
    private def collisionStep(collisionComp: CollisionComponent, velocityComp: VelocityComponent): Unit = {
      collisionComp.duration -= 1
      if (collisionComp.duration <= 0) {
        collisionComp.isColliding = false
      }
    }
  }

}
