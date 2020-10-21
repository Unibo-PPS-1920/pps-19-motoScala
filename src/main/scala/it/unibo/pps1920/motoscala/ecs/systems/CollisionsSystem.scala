package it.unibo.pps1920.motoscala.ecs.systems

import it.unibo.pps1920.motoscala.controller.EngineController
import it.unibo.pps1920.motoscala.controller.managers.audio.Clips
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.PlaySoundEffect
import it.unibo.pps1920.motoscala.controller.mediation.Event.{EntityLifeEvent, RedirectSoundEvent}
import it.unibo.pps1920.motoscala.controller.mediation.EventData.LifeData
import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}
import it.unibo.pps1920.motoscala.ecs.components._
import it.unibo.pps1920.motoscala.ecs.core.{Coordinator, ECSSignature}
import it.unibo.pps1920.motoscala.ecs.entities._
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.ecs.util.Side
import it.unibo.pps1920.motoscala.ecs.util.Side.{Left,Right, Center, Top, Bottom}
import it.unibo.pps1920.motoscala.ecs.{AbstractSystem, Entity, System}

import scala.math.signum
object CollisionsSystem {

  def apply(coordinator: Coordinator, controller: EngineController,
            fps: Int): System = new CollisionsSystemImpl(coordinator, controller, fps)

  private class CollisionsSystemImpl(coordinator: Coordinator, controller: EngineController, fps: Int)
    extends AbstractSystem(ECSSignature(classOf[PositionComponent],
                                        classOf[ShapeComponent],
                                        classOf[VelocityComponent],
                                        classOf[CollisionComponent])) {
    private val CollisionDuration = fps / 6

    private var entity1: Entity = _
    private var entity2: Entity = _


    override def update(): Unit = {
      var entitiesToCheck = entitiesRef()
      entitiesRef().filterNot(e => e.getClass.equals(classOf[JumpPowerUpEntity]) || e.getClass
        .equals(classOf[SpeedPowerUpEntity]) || e.getClass.equals(classOf[WeightPowerUpEntity]) || e.getClass
        .equals(classOf[PowerUpEntity])).foreach(e1 => {
        entitiesToCheck -= e1
        val colComp1 = coordinator.getEntityComponent[CollisionComponent](e1)
        val velComp1 = coordinator.getEntityComponent[VelocityComponent](e1)
        if (colComp1.isColliding) collisionStep(colComp1)
        else velComp1.currentVel = velComp1.inputVel
        entitiesToCheck.foreach(e2 => {
          if (!((e1.isInstanceOf[BumperCarEntity] && coordinator.getEntityComponent[JumpComponent](e1).isActive)
            || (e2.isInstanceOf[BumperCarEntity] && coordinator.getEntityComponent[JumpComponent](e2).isActive))) {
            val colComp2 = coordinator.getEntityComponent[CollisionComponent](e2)
            val velComp2 = coordinator.getEntityComponent[VelocityComponent](e2)
            val shapeComp1 = coordinator.getEntityComponent[ShapeComponent](e1)
            val shapeComp2 = coordinator.getEntityComponent[ShapeComponent](e2)
            val posComp1 = coordinator.getEntityComponent[PositionComponent](e1)
            val posComp2 = coordinator.getEntityComponent[PositionComponent](e2)
            if (colComp1.isColliding && colComp2.isColliding && (colComp1
              .collEntity == e2 && colComp2.collEntity == e1)) {
            } else {
              entity1 = e1
              entity2 = e2
              (e1, e2) match {
                case (bumperCar: BumperCarEntity, powerUp: PowerUpEntity) =>
                  if (areCirclesTouching(posComp1.pos, posComp2.pos, shapeComp1.shape.asInstanceOf[Circle]
                    .radius, shapeComp2.shape.asInstanceOf[Circle].radius)) {
                    addBumperToPowUp(bumperCar, powerUp)
                    entitiesToCheck -= powerUp
                    coordinator.removeEntityComponent(powerUp, colComp2)
                      .removeEntityComponent(powerUp, shapeComp2)
                      .removeEntityComponent(powerUp, posComp2)
                      .removeEntityComponent(powerUp, posComp2)
                  }
                case (powerUp: PowerUpEntity, bumperCar: BumperCarEntity) =>
                  if (areCirclesTouching(posComp2.pos, posComp1.pos, shapeComp2.shape
                    .asInstanceOf[Circle]
                    .radius, shapeComp1.shape.asInstanceOf[Circle].radius)) addBumperToPowUp(bumperCar, powerUp)
                case _ =>
                  checkCollision(e1, e2, shapeComp1.shape, shapeComp2.shape, colComp1, colComp2, posComp1
                                 , posComp2, velComp1, velComp2)
              }
            }
          }
        })
      })
    }

    private def compareAndCheckEntities(e1: Entity, otherEntities: Set[Entity], colComp1: CollisionComponent,
                                        velComp1: VelocityComponent): Unit ={


    }
    private def areCirclesTouching(cPos1: Vector2, cPos2: Vector2, cRadius1: Float, cRadius2: Float): Boolean =
      (cPos1 dist cPos2) <= (cRadius1 + cRadius2)

    private def checkCollision(e1: Entity, e2: Entity, shape1: Shape, shape2: Shape,
                               colComp1: CollisionComponent,
                               colComp2: CollisionComponent,
                               posComp1: PositionComponent,
                               posComp2: PositionComponent,
                               velComp1: VelocityComponent,
                               velComp2: VelocityComponent): Unit = {
      (shape1, shape2) match {
        case (Circle(radius1), Circle(radius2)) => if (areCirclesTouching(posComp1.pos, posComp2.pos, radius1, radius2)) {
          if (!(velComp2.currentVel.isZero() && velComp1.currentVel.isZero())) {
            colComp1.collEntity = e2
            colComp2.collEntity = e1
            collide(colComp1, colComp2, posComp1, posComp2, velComp1, velComp2)
            checkLifePoint(e1, e2, colComp1, colComp2, posComp1, posComp2, velComp1, velComp2)
            collisionStep(colComp1)
            collisionStep(colComp2)
          }
        }
        case (circle: Circle, rectangle: Rectangle) =>
          val inv = getDirInversion(circle, posComp1.pos, rectangle, posComp2.pos)
          if (inv != Vector2(1, 1)) {
            colComp1.isColliding = true
            colComp1.duration = CollisionDuration / 2
            checkLifePoint(e1, e2, colComp1, colComp2, posComp1, posComp2, velComp1, velComp2)
          }
          velComp1.currentVel = velComp1.currentVel mul inv

        case (rectangle: Rectangle, circle: Circle) =>
          val inv = getDirInversion(circle, posComp2.pos, rectangle, posComp1.pos)
          if (!(inv == Vector2(1, 1))) {
            colComp2.isColliding = true
            colComp2.duration = CollisionDuration / 2
            checkLifePoint(e1, e2, colComp1, colComp2, posComp1, posComp2, velComp1, velComp2)
          }
          velComp2.currentVel = velComp2.currentVel mul inv
        case _ => logger warn s"unexpected shape collision: $shape1 and $shape1"
      }
    }

    private def collide(colComp1: CollisionComponent,
                        colComp2: CollisionComponent,
                        posComp1: PositionComponent,
                        posComp2: PositionComponent,
                        velComp1: VelocityComponent,
                        velComp2: VelocityComponent): Unit = {
      controller.mediator.publishEvent(RedirectSoundEvent(PlaySoundEffect(Clips.Collision)))
      startCollision(colComp1, CollisionDuration )
      startCollision(colComp2, CollisionDuration)
      /* COLLISION CORE */
      if (colComp1.mass != 0 && colComp2.mass != 0) {
        // Compute unit normal and unit tangent vectors
        val normalVector = posComp2.pos sub posComp1.pos
        val unitNormalVector = normalVector.unitVector()
        val unitTangentVector = Vector2(-unitNormalVector.y, unitNormalVector.x)
        // Compute scalar projections of velocities onto unitNormalVector and unitTangentVector
        val normProjection1 = unitNormalVector dot velComp1.currentVel
        val tangProjection1 = unitTangentVector dot velComp1.currentVel
        val normProjection2 = unitNormalVector dot velComp2.currentVel
        val tangProjection2 = unitTangentVector dot velComp2.currentVel
        // Compute new normal velocities using one-dimensional elastic collision equations in the normal direction
        val newNormProjection1 = computeCollVel(normProjection1, normProjection2, colComp1
          .mass + 1, colComp2.mass)
        val newNormProjection2 = computeCollVel(normProjection2, normProjection1, colComp2
          .mass, colComp1.mass + 1)
        // Compute new normal and tangential velocity vectors
        val newNorVec1 = unitNormalVector.dot(newNormProjection1)
        val newTanVec1 = unitTangentVector.dot(tangProjection1)
        val newNorVec2 = unitNormalVector.dot(newNormProjection2)
        val newTanVec2 = unitTangentVector.dot(tangProjection2)
        // Set new velocities in x and y coordinates
        velComp1.currentVel = newNorVec1 add newTanVec1
        velComp2.currentVel = newNorVec2 add newTanVec2
      }
    }

    private def checkLifePoint(e1: Entity, e2: Entity,
                               colComp1: CollisionComponent,
                               colComp2: CollisionComponent,
                               posComp1: PositionComponent,
                               posComp2: PositionComponent,
                               velComp1: VelocityComponent,
                               velComp2: VelocityComponent): Unit = {
      def addDamage(userEntity: Entity, c1: CollisionComponent, c2: CollisionComponent): Unit = {
        c1.life -= c2.damage
        c2.life -= c1.damage
        controller.mediator.publishEvent(EntityLifeEvent(LifeData(userEntity, c1.life)))
      }

      (entity1, entity2) match {
        case (_: BumperCarEntity, _) => addDamage(entity1, colComp1, colComp2)
        case (_, _: BumperCarEntity) => addDamage(entity2, colComp2, colComp1)
        case (_: NabiconEntity, _: BumperCarEntity) => addDamage(entity1, colComp1, colComp2)
        case (_: NabiconEntity, _: BumperCarEntity) => addDamage(entity2, colComp2, colComp1)
        case _ =>
      }
    }

    private def getDirInversion(circle: Circle, circlePos: Vector2, rectangle: Rectangle,
                                rectanglePos: Vector2): Vector2 = {
      val topLeft =  rectanglePos sub Vector2(rectangle.dimX / 2, rectangle.dimY / 2 )
      val bottomRight = rectanglePos add Vector2(rectangle.dimX / 2, rectangle.dimY / 2 )

      val testEdge = Vector2(circlePos.x, circlePos.y)
      val invVec = Vector2(1, 1)

      val alignment = RectCollisionUtil.getSide(topLeft, bottomRight, circlePos)
      alignment.horizontalSide match {
        case Left =>     testEdge.x = topLeft.x; invVec.x = -1
        case Right =>    testEdge.x = bottomRight.x; invVec.x = -1
        case _=>
      }

      alignment.verticalSide match {
        case Top =>     testEdge.y = topLeft.y; invVec.y = -1
        case Bottom => testEdge.y = bottomRight.y; invVec.y = -1
        case _ =>
      }

      //check distance from closest edge
      if ((circlePos dist testEdge) <= circle.radius) {
        controller.mediator.publishEvent(RedirectSoundEvent(PlaySoundEffect(Clips.CollisionSoft)))
        logger debug s"inv vec: $invVec"
        invVec
      } //collide: direction inverted
      else Vector2(1, 1) //do not collide: same direction
    }


    private def computeCollVel(vel1: Double, vel2: Double, mass1: Double, mass2: Double): Double = {
      var newVel = (vel1 * (mass1 - mass2) + 2 * mass2 * vel2) / (mass1 + mass2)
      val newVelSign = signum(newVel)
      if (newVel.toInt == 0) newVel = 1.0 * newVelSign
      newVel
    }

    private def startCollision(colComp: CollisionComponent, duration: Int): Unit = {
      colComp.isColliding = true
      colComp.duration = duration
    }
    private def addBumperToPowUp(bumperCar: BumperCarEntity, powerUp: PowerUpEntity): Unit =
      coordinator.getEntityComponent[PowerUpComponent](powerUp).entity = Some(bumperCar)

    private def collisionStep(collisionComp: CollisionComponent): Unit = {
      collisionComp.duration -= 1
      if (collisionComp.duration <= 0) collisionComp.isColliding = false
    }
  }
}

object RectCollisionUtil {
  def getSide(refTopLeft: Vector2, refBottomRight: Vector2, point: Vector2): Alignment ={

    val alignment = Alignment(Center, Center)
    if (point.x < refTopLeft.x) alignment.horizontalSide = Side.Left
    else if (point.x > refBottomRight.x) alignment.horizontalSide = Side.Right

    if (point.y < refTopLeft.y) alignment.verticalSide = Side.Top
    else if (point.y > refBottomRight.y) alignment.verticalSide = Side.Bottom
    alignment
  }
}

sealed case class Alignment(var horizontalSide: Side, var verticalSide: Side)