package it.unibo.pps1920.motoscala.controller.managers.audio


/** A MediaEvent represents one single action to be performed on the [[SoundAgentLogic]] provided. */
sealed trait MediaEvent {
  /** The action to be performed. */
  def handle(sl: SoundAgentLogic): Unit
}
object MediaEvent {
  final case class PlayMusicEvent(media: Music) extends MediaEvent {
    override def handle(sl: SoundAgentLogic): Unit = sl.playMusic(media)
  }
  final case class PlaySoundEffect(clip: Clips) extends MediaEvent {
    override def handle(sl: SoundAgentLogic): Unit = sl.playClip(clip)
  }
  final case class SetVolumeMusic(value: Double) extends MediaEvent {
    override def handle(sl: SoundAgentLogic): Unit = sl.setVolumeMusic(value)
  }
  final case class SetVolumeEffect(value: Double) extends MediaEvent {
    override def handle(sl: SoundAgentLogic): Unit = sl.setVolumeEffect(value)
  }
  final case class StopMusic() extends MediaEvent {
    override def handle(sl: SoundAgentLogic): Unit = sl.stopMusic()
  }
  final case class PauseMusic() extends MediaEvent {
    override def handle(sl: SoundAgentLogic): Unit = sl.pauseMusic()
  }
  final case class RestartMusic() extends MediaEvent {
    override def handle(sl: SoundAgentLogic): Unit = sl.restartMusic()
  }
  final case class ResumeMusic() extends MediaEvent {
    override def handle(sl: SoundAgentLogic): Unit = sl.resumeMusic()
  }
}




