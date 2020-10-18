package it.unibo.pps1920.motoscala.model

import scala.collection.immutable.HashMap

object Scores {
  sealed trait Scores
  case class ScoresData(scoreTable: HashMap[String, Int] = HashMap()) extends Scores

}

