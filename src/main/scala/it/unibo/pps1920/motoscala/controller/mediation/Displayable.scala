package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.controller.mediation.Event._

/** This interfaced wraps an [[EventObserver]] for DisplayableEvent. */
trait Displayable extends EventObserver[DisplayableEvent] {

  /** Notify the observer with data from [[it.unibo.pps1920.motoscala.controller.mediation.Event.DrawEntityEvent]].
   *
   * @param players the set of players
   * @param entities the set of entities
   */
  def notifyDrawEntities(players: Set[Option[EntityData]], entities: Set[EntityData]): Unit

  /** Notify the observer with data from [[it.unibo.pps1920.motoscala.controller.mediation.Event.LevelEndEvent]]
   *
   * @param data the data containing end level information
   */
  def notifyLevelEnd(data: EndData): Unit

  /** Notify the observer with data from [[it.unibo.pps1920.motoscala.controller.mediation.Event.EntityLifeEvent]]
   *
   * @param data the data containing life information
   */
  def notifyEntityLife(data: LifeData): Unit

  /** Notify the observer with data from [[it.unibo.pps1920.motoscala.controller.mediation.Event.RedirectSoundEvent]]
   *
   * @param event the sound event
   */
  def notifyRedirectSound(event: SoundEvent): Unit

  override def notify(event: DisplayableEvent): Unit = event match {
    case DrawEntityEvent(player, entities) => notifyDrawEntities(player, entities)
    case LevelEndEvent(data) => notifyLevelEnd(data)
    case EntityLifeEvent(data) => notifyEntityLife(data)
    case RedirectSoundEvent(event) => notifyRedirectSound(event)
  }
}
