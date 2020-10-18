package it.unibo.pps1920.motoscala.ecs.util

import org.slf4j.LoggerFactory

import scala.math.{pow, sqrt}
/**
 * 2d vector
 */
trait Vector2 {

  var x: Double
  var y: Double
  private val logger = LoggerFactory getLogger classOf[Vector2]
  def add(vector2: Vector2): Vector2 = Vector2(x + vector2.x, y + vector2.y)

  def add(scalar: Double): Vector2 = Vector2(x + scalar, y + scalar)

  def dist(vector2: Vector2): Double = sqrt(pow(x - vector2.x, 2) + pow(y - vector2.y, 2))

  def sub(vector2: Vector2): Vector2 = Vector2(x - vector2.x, y - vector2.y)

  def clip(): Vector2 = Vector2(if (x != 0) x / x.abs else x, if (y != 0) y / y.abs else y)

  def mul(v: Vector2): Vector2 = Vector2(x * v.x, y * v.y)

  def div(vector2: Vector2): Vector2 = Vector2(x / vector2.x, y / vector2.y)

  def dot(v: Vector2): Double = x * v.x + y * v.y

  def dot(scalar: Double): Vector2 = Vector2(scalar * x, scalar * y)

  def abs(): Vector2 = Vector2(x.abs, y.abs)

  def neg(): Vector2 = Vector2(-x, -y)

  def sumabs(v: Vector2): Vector2 = {
    val vc = Vector2(x.abs + v.x.abs, y.abs + v.y.abs) mul clip()
    logger debug (s"Before zero collision: velocity ent1: ${vc}")
    vc
  }

  def magnitude(): Double = sqrt(x * x + y * y)

  def isZero(): Boolean = x == 0 && y == 0

  def unitVector(): Vector2 = {
    val mag = magnitude()
    if (mag != 0) Vector2(x / mag, y / mag) else Vector2(0, 0)
  }
}

object Vector2 {

  implicit def tuple2ToVector2(tuple2: (Double, Double)): Vector2 = Vector2(tuple2._1, tuple2._2)
  implicit def tuple2IntToVector2(tuple2: (Int, Int)): Vector2 = Vector2(tuple2._1, tuple2._2)

  def apply(x: Double, y: Double): Vector2 = Vector2Impl(x, y)
  private case class Vector2Impl(override var x: Double, override var y: Double) extends Vector2 {
  }

}


