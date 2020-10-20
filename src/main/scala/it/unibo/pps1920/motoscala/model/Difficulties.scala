package it.unibo.pps1920.motoscala.model


object Difficulties extends Enumeration {
  val EASY = Difficult("Easy", 1)
  val MEDIUM = Difficult("Medium", 2)
  val HARD = Difficult("Hard", 3)
  protected case class Difficult(name: String, number: Int) extends super.Val()
}
