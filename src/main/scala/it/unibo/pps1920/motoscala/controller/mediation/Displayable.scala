package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.controller.mediation.Event._

/**
 * This interfaced wraps an [[EventObserver]] for [[DisplayableEvent]].
 */
trait Displayable extends EventObserver[DisplayableEvent] {
  def notifyDrawEntities(players: Set[Option[EntityData]], entities: Set[EntityData]): Unit
  def notifyLevelEnd(data: EndData): Unit
  def notifyEntityLife(data: LifeData): Unit
  def notifyRedirectSound(event: SoundEvent): Unit
  override def notify(event: DisplayableEvent): Unit = event match {
    case DrawEntityEvent(player, entities) => notifyDrawEntities(player, entities)
    case LevelEndEvent(data) => notifyLevelEnd(data)
    case EntityLifeEvent(data) => notifyEntityLife(data)
    case RedirectSoundEvent(event) => notifyRedirectSound(event)
  }
}
