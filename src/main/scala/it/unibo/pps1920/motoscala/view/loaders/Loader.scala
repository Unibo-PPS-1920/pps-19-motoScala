package it.unibo.pps1920.motoscala.view.loaders

/** Flyweight loader that caches the requested values in a map
 *
 * @tparam A the keys type
 * @tparam B the values type
 */
protected[view] trait Loader[A, B] {

  /** The cache which holds the already requested values */
  val cache = new collection.mutable.HashMap[A, B]()

  /** This method returns the value requested by key, or computes a function to update and return it.
   *
   * @param a the key
   * @param f the function, from A to B
   * @return the element stored by key a
   */
  protected def get(a: A, f: A => B): B = cache.getOrElseUpdate(a, f(a))
}
