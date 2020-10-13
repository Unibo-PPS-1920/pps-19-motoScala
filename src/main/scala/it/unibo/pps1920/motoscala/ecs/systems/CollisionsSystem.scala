package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}
import it.unibo.pps1920.motoscala.ecs.components.{CollisionComponent, PositionComponent, ShapeComponent, VelocityComponent}
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.{Direction, Vector2}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Component, Entity, System}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
object CollisionsSystem {

  def apply(coordinator: Coordinator, fps: Int): System = new CollisionsSystemImpl(coordinator, fps)

  private class CollisionsSystemImpl(coordinator: Coordinator, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent],
                                        classOf[ShapeComponent],
                                        classOf[VelocityComponent],
                                        classOf[CollisionComponent])) {


    private val CollisionDuration = (fps / 2)
    private val CollisionVelocity = Vector2(5, 5)

    override def update(): Unit = {
      var entitiesToCheck = entitiesRef()
      entitiesRef().foreach(e1 => {
        entitiesToCheck -= e1
        val collisionCompE1 = extractComponent[CollisionComponent](e1, classOf[CollisionComponent])
        val velocityCompE1 = extractComponent[VelocityComponent](e1, classOf[VelocityComponent])
        if (collisionCompE1.isColliding) {
          collisionStep(collisionCompE1, velocityCompE1)

        } else {
          velocityCompE1.vel = velocityCompE1.newVel
        }
        if (e1.isInstanceOf[BumperCarEntity]) logger warn "\n velocità" + velocityCompE1.toString + "\n entità" + e1
          .toString
        entitiesToCheck.foreach(e2 => {
          if (isTouching(e1, e2)) {
            collide(e1, e2)
          }
        })
      })
    }

    private def isTouching(e1: Entity, e2: Entity): Boolean = {
      val e1ShapeC = extractComponent[ShapeComponent](e1, classOf[ShapeComponent])
      val e2ShapeC = extractComponent[ShapeComponent](e2, classOf[ShapeComponent])
      val e1PosC = extractComponent[PositionComponent](e1, classOf[PositionComponent])
      val e2PosC = extractComponent[PositionComponent](e2, classOf[PositionComponent])

      (e1ShapeC.shape, e2ShapeC.shape) match {
        case (Circle(e1Radius), Circle(e2Radius)) => (e1PosC.pos dist e2PosC.pos) <= (e1Radius + e2Radius)
        case (Circle(radius), Rectangle(dimX, dimY)) => ???
        case _ => logger warn s"unexpected shape collision: ${e1ShapeC.shape} and ${e1ShapeC.shape}"; false
      }

    }

    private def collide(e1: Entity, e2: Entity): Unit = {

      val collisionCompE1 = extractComponent[CollisionComponent](e1, classOf[CollisionComponent])
      val collisionCompE2 = extractComponent[CollisionComponent](e2, classOf[CollisionComponent])
      val velocityCompE1 = extractComponent[VelocityComponent](e1, classOf[VelocityComponent])
      val velocityCompE2 = extractComponent[VelocityComponent](e2, classOf[VelocityComponent])
      val positionCompE1 = extractComponent[PositionComponent](e1, classOf[PositionComponent])
      val positionCompE2 = extractComponent[PositionComponent](e2, classOf[PositionComponent])
      startCollision(collisionCompE1, collisionCompE2, velocityCompE1, velocityCompE2)

      /** **_____________________________________________________***** */

      if (collisionCompE1.mass != 0 && collisionCompE2.mass != 0) {
        logger debug(s"Before collision: velocity ent1: ${velocityCompE1.vel}  velocity ent2 = ${velocityCompE2.vel}")

        // Compute unit normal and unit tangent vectors
        val normalVector = positionCompE2.pos sub positionCompE1.pos
        val unitNormalVector = normalVector.unitVector()
        val unitTangentVector = Vector2(-unitNormalVector.y, unitNormalVector.x)
        // Compute scalar projections of velocities onto unitNormalVector and unitTangentVector
        val normProj1 = unitNormalVector dot velocityCompE1.vel
        val tangProj1 = unitTangentVector dot velocityCompE1.vel
        val normProj2 = unitNormalVector dot velocityCompE2.vel
        val tangProj2 = unitTangentVector dot velocityCompE2.vel

        // Compute new normal velocities using one-dimensional elastic collision equations in the normal direction
        val newNormProj1 = computeCollVel(normProj1, normProj2, collisionCompE1.mass, collisionCompE2.mass)
        val newNormProj2 = computeCollVel(normProj2, normProj1, collisionCompE2.mass, collisionCompE1.mass)

        // Compute new normal and tangential velocity vectors
        val newNorVec1 = unitNormalVector.dot(newNormProj1)
        val newTanVec1 = unitTangentVector.dot(tangProj1)
        val newNorVec2 = unitNormalVector.dot(newNormProj2)
        val newTanVec2 = unitTangentVector.dot(tangProj2)

        // Set new velocities in x and y coordinates

        velocityCompE1.vel = Direction.vecToDir(newNorVec1 add newTanVec1).value.mul(CollisionVelocity)
        velocityCompE2.vel = Direction.vecToDir(newNorVec2 add newTanVec2).value.mul(CollisionVelocity)
        logger debug(s"Computed collision: velocity ent1: ${velocityCompE1.vel}  velocity ent2 = ${velocityCompE2.vel}")


      }

    }

    private def computeCollVel(vel1: Double, vel2: Double, mass1: Double, mass2: Double): Double =
      (vel1 * (mass1 - mass2) + 2 * mass2 * vel2) / (mass1 + mass2)

    private def startCollision(collisionCompE1: CollisionComponent, collisionCompE2: CollisionComponent,
                               velCompE1: VelocityComponent, velCompE2: VelocityComponent): Unit = {
      collisionCompE1.isColliding = true
      collisionCompE2.isColliding = true
      collisionCompE1.duration = CollisionDuration
      collisionCompE2.duration = CollisionDuration
      collisionCompE1.oldSpeed = velCompE1.vel.abs()
      collisionCompE2.oldSpeed = velCompE2.vel.abs()
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
        collisionComp.isColliding = false;
        velocityComp.vel = collisionComp.oldSpeed mul velocityComp.vel.dir()
      }
    }
  }

}
