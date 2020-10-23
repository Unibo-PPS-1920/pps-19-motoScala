package it.unibo.pps1920.motoscala.model

import scala.collection.immutable.HashMap
/** Represent one scores table, like : name - score. */
sealed trait Scores

/** Provide default score data instance. */
object Scores {

  /** contains all the player names and their respectively score.
   *
   * @param scoreTable the score map
   */
  case class ScoresData(var scoreTable: HashMap[String, Int] = HashMap()) extends Scores

}


