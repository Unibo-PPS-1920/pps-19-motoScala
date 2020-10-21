package it.unibo.pps1920.motoscala.ecs.util


import scala.math.{pow, sqrt}
/**
 * 2d vector
 */
trait Vector2 {

  var x: Double
  var y: Double
  /**
   * Sums two vectors
   *
   * @param vector2 vector to be summed
   * @return sum vector
   */
  def add(vector2: Vector2): Vector2 = Vector2(x + vector2.x, y + vector2.y)
  /**
   * Sums a scalar to a vector
   *
   * @param scalar scalar to be summed
   * @return resulting vector
   */
  def add(scalar: Double): Vector2 = Vector2(x + scalar, y + scalar)
  /**
   * Computes the distance between two vectors
   *
   * @param vector2 other vector
   * @return distance
   */
  def dist(vector2: Vector2): Double = sqrt(pow(x - vector2.x, 2) + pow(y - vector2.y, 2))
  /**
   * Subtracts two vectors
   *
   * @param vector2 vector to be subtracted
   * @return resulting vector
   */
  def sub(vector2: Vector2): Vector2 = Vector2(x - vector2.x, y - vector2.y)
  /**
   * Outputs the unit vector of a given vector
   *
   * @return unit vector
   */
  def clip(): Vector2 = Vector2(if (x != 0) x / x.abs else x, if (y != 0) y / y.abs else y)
  /**
   * Multiplies two vectors with eachother
   *
   * @param vector2 other vector
   * @return resulting vector
   */
  def mul(vector2: Vector2): Vector2 = Vector2(x * vector2.x, y * vector2.y)
  /**
   * Divides two vectors with eachother
   *
   * @param vector2 other vector
   * @return resulting vector
   */
  def div(vector2: Vector2): Vector2 = Vector2(x / vector2.x, y / vector2.y)
  /**
   * Dot product between two vectors
   *
   * @param vector2 other vector
   * @return dot product
   */
  def dot(vector2: Vector2): Double = x * vector2.x + y * vector2.y
  /**
   * Dot product between a vector and a scalar
   *
   * @param scalar scalar
   * @return dot product
   */
  def dot(scalar: Double): Vector2 = Vector2(scalar * x, scalar * y)
  /**
   * Absolute value of a vector
   *
   * @return absolute vector
   */
  def abs(): Vector2 = Vector2(x.abs, y.abs)
  /**
   * Negative of a vector
   *
   * @return inverted vector
   */
  def neg(): Vector2 = Vector2(-x, -y)
  /**
   * Absolute sum between two vectors
   *
   * @param vector2 other vector
   * @return absolute sum vector
   */
  def sumAbs(vector2: Vector2): Vector2 = Vector2(x.abs + vector2.x.abs, y.abs + vector2.y.abs) mul clip()

  /**
   * Computes the magnitude of a vector
   *
   * @return the magnitude
   */
  def magnitude(): Double = sqrt(x * x + y * y)
  /**
   * Computes if vector is zero
   *
   * @return true if both components are zero, false otherwise
   */
  def isZero: Boolean = x == 0 && y == 0
  /**
   * Computes the unit vector
   *
   * @return the unit vector
   */
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


