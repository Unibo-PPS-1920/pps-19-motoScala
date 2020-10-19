package it.unibo.pps1920.motoscala.controller.mediation

import it.unibo.pps1920.motoscala.controller.mediation.Event._
import it.unibo.pps1920.motoscala.controller.mediation.EventData.LevelSetupData
/**
 * This interfaced wraps an [[EventObserver]] for [[DisplayableEvent]].
 */
trait Displayable extends EventObserver[DisplayableEvent] {
  def notifyDrawEntities(players: Set[Option[EntityData]], entities: Set[EntityData]): Unit
  def notifyLevelSetup(data: LevelSetupData): Unit
  def notifyLevelEnd(data: LevelEndData): Unit
  def notifyRedirectSound(event: SoundEvent): Unit
  override def notify(event: DisplayableEvent): Unit = event match {
    case DrawEntityEvent(player, entities) => notifyDrawEntities(player, entities)
    case LevelSetupEvent(data) => notifyLevelSetup(data)
    case LevelEndEvent(data) => notifyLevelEnd(data)
    case RedirectSoundEvent(event) => notifyRedirectSound(event)
  }
}
