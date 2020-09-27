package it.unibo.pps1920.motoscala.ecs.util

trait Vector2 {

  def x: Double
  def y: Double

  def add(vector2: Vector2): Vector2 = Vector2(x + vector2.x, y + vector2.y)

  def add(scalar: Double): Vector2 = Vector2(x + scalar, y + scalar)

  def versor(): Vector2 = Vector2(if (x != 0) x / x.abs else x, if (y != 0) y / y.abs else y)

  def mul(scalar: Double): Vector2 = Vector2(x * scalar, y * scalar)

  def div(vector2: Vector2): Vector2 = Vector2(x / vector2.x, y / vector2.y)
}

object Vector2 {

  def apply(x: Double, y: Double): Vector2 = Vector2Impl(x, y)

  private case class Vector2Impl(override val x: Double, override val y: Double) extends Vector2 {
  }

}

