package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.controller.mediation.Event.RedirectSoundEvent
import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.entities.{BumperCarEntity, PowerUpEntity}
import it.unibo.pps1920.motoscala.ecs.managers.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Entity, System}

import scala.math.signum
import scala.reflect.ClassTag
object CollisionsSystem {

  def apply(coordinator: Coordinator, controller: EngineController,
            fps: Int): System = new CollisionsSystemImpl(coordinator, controller, fps)

  private class CollisionsSystemImpl(coordinator: Coordinator, controller: EngineController, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent],
                                        classOf[ShapeComponent],
                                        classOf[VelocityComponent],
                                        classOf[CollisionComponent])) {

    private val CollisionDuration = fps / 8
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
      entitiesRef().filter(_.getClass != classOf[PowerUpEntity]).foreach(e1 => {
        import it.unibo.pps1920.motoscala.ecs.components.JumpComponent
        entitiesToCheck -= e1
        //        logger info s"e1: ${e1.getClass}"

        collisionCompE1 = extractComponent[CollisionComponent](e1)
        velocityCompE1 = extractComponent[VelocityComponent](e1)

        if (collisionCompE1.isColliding) collisionStep(collisionCompE1, velocityCompE1)
        else velocityCompE1.currentVel = velocityCompE1.inputVel


        entitiesToCheck.foreach(e2 => {
          //check collision only if jumpComponent is not active
          if (!((e1.isInstanceOf[BumperCarEntity] && extractComponent[JumpComponent](e1)
            .isActive)
            || (e2.isInstanceOf[BumperCarEntity] && extractComponent[JumpComponent](e2)
            .isActive))) {
            //            logger info s"e2: ${e2.getClass}"
            collisionCompE2 = extractComponent[CollisionComponent](e2)
            velocityCompE2 = extractComponent[VelocityComponent](e2)
            shapeCompE1 = extractComponent[ShapeComponent](e1)
            shapeCompE2 = extractComponent[ShapeComponent](e2)
            positionCompE1 = extractComponent[PositionComponent](e1)
            positionCompE2 = extractComponent[PositionComponent](e2)
            (e1, e2) match {
              case (bumperCar: BumperCarEntity, powerUp: PowerUpEntity) =>
                if (areCirclesTouching(positionCompE1.pos, positionCompE2.pos, shapeCompE1.shape.asInstanceOf[Circle]
                  .radius, shapeCompE2.shape.asInstanceOf[Circle].radius)) {
                  addBumperToPowUp(bumperCar, powerUp)
                  entitiesToCheck -= powerUp
                  //                  logger info "bbbbb"
                  coordinator.removeEntityComponent(powerUp, collisionCompE2)
                    .removeEntityComponent(powerUp, shapeCompE2)
                    .removeEntityComponent(powerUp, positionCompE2)
                    .removeEntityComponent(powerUp, positionCompE2)
                  //                  logger info "aaaaaa"
                }
              case (powerUp: PowerUpEntity, bumperCar: BumperCarEntity) =>
                if (areCirclesTouching(positionCompE2.pos, positionCompE1.pos, shapeCompE2.shape.asInstanceOf[Circle]
                  .radius, shapeCompE1.shape.asInstanceOf[Circle].radius)) {
                  addBumperToPowUp(bumperCar, powerUp)
                  logger info "succede"
                  //                  entitiesToCheck -= powerUp
                  //                  coordinator.removeEntity(powerUp)
                }
              case _ =>
                checkCollision(e1, e2, shapeCompE1.shape, shapeCompE2.shape, positionCompE1.pos, positionCompE2
                  .pos, velocityCompE1, velocityCompE2)
            }
          }
        })
      })
    }

    private def areCirclesTouching(cPos1: Vector2, cPos2: Vector2, cRadius1: Float, cRadius2: Float): Boolean =
      (cPos1 dist cPos2) <= (cRadius1 + cRadius2)
    //      (positionCompE1.pos dist positionCompE2.pos) <= (shapeCompE1.shape.asInstanceOf[Circle].radius
    //        + shapeCompE2.shape.asInstanceOf[Circle].radius)

    private def checkCollision(e1: Entity, e2: Entity, shape1: Shape, shape2: Shape, pos1: Vector2, pos2: Vector2,
                               vel1: VelocityComponent,
                               vel2: VelocityComponent): Unit = {
      (shape1, shape2) match {
        case (Circle(radius1), Circle(radius2)) => if (areCirclesTouching(pos1, pos2, radius1, radius2)) {
          if (!(vel2.currentVel.isZero() && vel1.currentVel.isZero())) {
            collisionCompE1.collEntity = e2
            collisionCompE2.collEntity = e1
            collide()
            collisionStep(collisionCompE1, vel1)
            collisionStep(collisionCompE2, vel2)
          }
        }
        case (circle: Circle, rectangle: Rectangle) => vel1.currentVel = vel1
          .currentVel mul getDirInversion(circle, pos1, rectangle, pos2)
        case (rectangle: Rectangle, circle: Circle) => vel2.currentVel = vel2
          .currentVel mul getDirInversion(circle, pos2, rectangle, pos1)
        case _ => logger warn s"unexpected shape collision: ${shape1} and ${shape1}"
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
      val wereBothColliding = collisionCompE1.isColliding && collisionCompE2.isColliding
      controller.mediator.publishEvent(RedirectSoundEvent(PlaySoundEffect(Clips.Collision)))

      startCollision()

      /* COLLISION CORE */

      if (collisionCompE1.mass != 0 && collisionCompE2.mass != 0) {
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
        //logger info s"Vel1: (${velocityCompE1.currentVel.x.toInt}, ${velocityCompE1.currentVel.y.toInt})" +
        //s"(${velocityCompE2.currentVel.x.toInt}, ${velocityCompE2.currentVel.y.toInt})"
      }
    }

    private def computeCollVel(vel1: Double, vel2: Double, mass1: Double, mass2: Double): Double = {
      var newVel = (vel1 * (mass1 - mass2) + 2 * mass2 * vel2) / (mass1 + mass2)
      val newVelSign = signum(newVel)
      if (newVel.toInt == 0) newVel = 1.0 * newVelSign
      newVel
    }

    private def startCollision(): Unit = {
      collisionCompE1.isColliding = true
      collisionCompE2.isColliding = true
      collisionCompE1.duration = CollisionDuration
      collisionCompE2.duration = CollisionDuration
    }
    private def extractComponent[T: ClassTag](e: Entity): T =
      coordinator.getEntityComponent(e, implicitly[ClassTag[T]].runtimeClass).get.asInstanceOf[T]

    private def addBumperToPowUp(bumperCar: BumperCarEntity, powerUp: PowerUpEntity): Unit =
      extractComponent[PowerUpComponent](powerUp).entity = Some(bumperCar)

    //Performs a collision step, decrementing the collision duration and handling termination
    private def collisionStep(collisionComp: CollisionComponent, velocityComp: VelocityComponent): Unit = {
      collisionComp.duration -= 1
      if (collisionComp.duration <= 0) collisionComp.isColliding = false

    }
  }

}
