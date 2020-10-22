package it.unibo.pps1920.motoscala.ecs.systems
import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}
import it.unibo.pps1920.motoscala.ecs.util.{Side, Vector2}
import it.unibo.pps1920.motoscala.ecs.util.Side.Center
import it.unibo.pps1920.motoscala.ecs.util.Alignment
import scala.math.signum
package object Collision {

  def elasticCollision1D(vel1: Double, vel2: Double, mass1: Double, mass2: Double): Double = {
    var newVel = (vel1 * (mass1 - mass2) + 2 * mass2 * vel2) / (mass1 + mass2)
    val newVelSign = signum(newVel)
    if (newVel.toInt == 0) newVel = 1.0 * newVelSign
    newVel
  }
  def areCirclesTouching(cPos1: Vector2, cPos2: Vector2, cRadius1: Float, cRadius2: Float): Boolean =
    (cPos1 dist cPos2) <= (cRadius1 + cRadius2)
  def getSide(refTopLeft: Vector2, refBottomRight: Vector2, point: Vector2): Alignment ={
    val alignment = Alignment(Center, Center)
    if (point.x < refTopLeft.x) alignment.horizontalSide = Side.Left
    else if (point.x > refBottomRight.x) alignment.horizontalSide = Side.Right

    if (point.y < refTopLeft.y) alignment.verticalSide = Side.Top
    else if (point.y > refBottomRight.y) alignment.verticalSide = Side.Bottom
    alignment
  }
  def getDirInversion(circle: Circle, circlePos: Vector2, rectangle: Rectangle,
                      rectanglePos: Vector2): Vector2 = {
    val topLeft =  rectanglePos sub Vector2(rectangle.dimX / 2, rectangle.dimY / 2 )
    val bottomRight = rectanglePos add Vector2(rectangle.dimX / 2, rectangle.dimY / 2 )

    val testEdge = Vector2(circlePos.x, circlePos.y)
    val invVec = Vector2(1, 1)

    val alignment = Collision.getSide(topLeft, bottomRight, circlePos)
    alignment.horizontalSide match {
      case Side.Left =>     testEdge.x = topLeft.x; invVec.x = -1
      case Side.Right =>    testEdge.x = bottomRight.x; invVec.x = -1
      case _=>
    }

    alignment.verticalSide match {
      case Side.Top =>     testEdge.y = topLeft.y; invVec.y = -1
      case Side.Bottom => testEdge.y = bottomRight.y; invVec.y = -1
      case _ =>
    }

    //check distance from closest edge
    if ((circlePos dist testEdge) <= circle.radius) {

      invVec
    } //collide: direction inverted
    else Vector2(1, 1) //do not collide: same direction
  }
}
