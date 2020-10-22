package it.unibo.pps1920.motoscala.engine

/**
 * Engine for gameloop
 */
trait UpdatableEngine {
  /**
   * advances the state by one tick
   */
  def tick(): Unit
}