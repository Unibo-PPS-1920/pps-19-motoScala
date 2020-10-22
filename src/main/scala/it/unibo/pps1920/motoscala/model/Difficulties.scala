package it.unibo.pps1920.motoscala.model

/** Provide a default [[Difficult]] Instance */
object Difficulties extends Enumeration {
  val EASY: Difficult = Difficult("Easy", 1)
  val MEDIUM: Difficult = Difficult("Medium", 2)
  val HARD: Difficult = Difficult("Hard", 3)

  val difficultiesList: List[Difficult] =
    List(
      EASY,
      MEDIUM,
      HARD
      )
  /** A [[Difficult]] represents the difficult on the game, it contains name and one incremental value.
   *
   * @param name the difficult name
   * @param number the difficult index
   */
  protected case class Difficult(name: String, number: Int) extends super.Val()

}
