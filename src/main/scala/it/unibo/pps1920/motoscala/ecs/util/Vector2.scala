package it.unibo.pps1920.motoscala.ecs.util

import scala.math.{pow, sqrt}

/**
 * 2d vector
 */
trait Vector2 {

  var x: Double
  var y: Double

  def add(vector2: Vector2): Vector2 = Vector2(x + vector2.x, y + vector2.y)

  def add(scalar: Double): Vector2 = Vector2(x + scalar, y + scalar)

  def dist(vector2: Vector2): Double = sqrt(pow(x - vector2.x, 2) + pow(y - vector2.y, 2))
  def sub(vector2: Vector2): Vector2 = Vector2(x - vector2.x, y - vector2.y)

  def unit(): Vector2 = Vector2(if (x != 0) x / x.abs else x, if (y != 0) y / y.abs else y)

  def mul(scalar: Double): Vector2 = Vector2(x * scalar, y * scalar)

  def div(vector2: Vector2): Vector2 = Vector2(x / vector2.x, y / vector2.y)

  def dot(v1: Vector2, v2: Vector2): Double = v1.x * v2.x + v1.y * v2.y
}

object Vector2 {

  implicit def tuple2ToVector2(tuple2: (Double, Double)): Vector2 = Vector2(tuple2._1, tuple2._2)

  def apply(x: Double, y: Double): Vector2 = Vector2Impl(x, y)

  private case class Vector2Impl(override var x: Double, override var y: Double) extends Vector2 {
  }

}


