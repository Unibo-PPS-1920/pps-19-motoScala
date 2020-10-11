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

  def dir(): Vector2 = Vector2(if (x != 0) x / x.abs else x, if (y != 0) y / y.abs else y)

  def mul(v: Vector2): Vector2 = Vector2(x * v.x, y * v.y)

  def div(vector2: Vector2): Vector2 = Vector2(x / vector2.x, y / vector2.y)

  def dot(v: Vector2): Double = x * v.x + y * v.y

  def dot(scalar: Double): Vector2 = Vector2(scalar*x, scalar*y)

  def magnitude() : Double = sqrt(x*x + y*y)

  def unitVector() : Vector2 = {
      val mag = magnitude()
      if(mag != 0) Vector2(x/mag, y/mag) else Vector2(0,0)
  }
}

object Vector2 {

  def apply(x: Double, y: Double): Vector2 = Vector2Impl(x, y)

  private case class Vector2Impl(override var x: Double, override var y: Double) extends Vector2 {
  }

}


