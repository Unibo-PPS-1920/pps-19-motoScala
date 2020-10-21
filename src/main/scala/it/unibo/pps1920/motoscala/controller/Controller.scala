package it.unibo.pps1920.motoscala.controller

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, ExtendedActorSystem}
import com.typesafe.config.ConfigFactory
import it.unibo.pps1920.motoscala
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.{SetVolumeEffect, SetVolumeMusic}
import it.unibo.pps1920.motoscala.controller.managers.audio.{MediaEvent, SoundAgent}
import it.unibo.pps1920.motoscala.controller.managers.file.DataManager
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.ecs.entities.BumperCarEntity
import it.unibo.pps1920.motoscala.engine.Engine
import it.unibo.pps1920.motoscala.model.Difficulties.difficultiesList
import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.model.Scores.ScoresData
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
import it.unibo.pps1920.motoscala.model.{Level, MultiPlayerSetup, NetworkAddr}
import it.unibo.pps1920.motoscala.multiplayer.actors.{ClientActor, ServerActor}
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage._
import it.unibo.pps1920.motoscala.multiplayer.messages.DataType
import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
import it.unibo.pps1920.motoscala.view.events.ViewEvent.{LevelDataEvent, LevelSetupData, LevelSetupEvent, ScoreDataEvent, SettingsDataEvent, SetupLobbyEvent, _}
import it.unibo.pps1920.motoscala.view.{JavafxEnums, ObserverUI, showNotificationPopup}
import javafx.application.Platform
import org.slf4j.LoggerFactory

import scala.collection.immutable.HashMap

trait Controller extends ActorController with SoundController with EngineController with ObservableUI {
}
object Controller {
  def apply(): Controller = new ControllerImpl()

  private class ControllerImpl private[Controller](
    override val mediator: Mediator = Mediator()) extends Controller {

    private val logger = LoggerFactory getLogger classOf[ControllerImpl]


    private val maxPlayers = 4
    private val dataManager: DataManager = new DataManager()
    private val config = ConfigFactory.load("application")
    private val system = ActorSystem("MotoSystem", config)
    private val soundAgent: SoundAgent = SoundAgent()
    private var score: Int = 0

    private var engine: Option[Engine] = None
    this.dataManager.initAppDirectory()
    private var observers: Set[ObserverUI] = Set()
    private var levels: List[LevelData] = List()
    private var actualSettings: SettingsData = loadSettings()
    private var clientActor: Option[ActorRef] = None
    private var serverActor: Option[ActorRef] = None
    private var matchSetupMp: Option[MultiPlayerSetup] = None
    private var multiplayerStatus: Boolean = false
    this.soundAgent.start()

    this.setAudioVolume(this.actualSettings.musicVolume, actualSettings.effectVolume)


    override def attachUI(obs: ObserverUI*): Unit = observers = observers ++ obs
    override def detachUI(obs: ObserverUI*): Unit = observers = observers -- obs
    override def start(): Unit = {
      score = 0
      engine.get.start()
    }
    override def pause(): Unit = engine.get.pause()
    override def resume(): Unit = engine.get.resume()
    override def stop(): Unit = {
      if (engine.isDefined) {
        engine.get.stop()
        engine = None
      }

    }
    override def redirectSoundEvent(me: MediaEvent): Unit = this.soundAgent.enqueueEvent(me)
    override def loadSetting(): Unit = observers
      .foreach(observer => observer.notify(SettingsDataEvent(this.actualSettings)))
    override def lobbyInfoChanged(level: Option[Int] = None, difficult: Option[Int] = None,
                                  isStatusChanged: Boolean = false): Unit = {
      if (isStatusChanged) this.multiplayerStatus = !this.multiplayerStatus

      if (serverActor.isDefined && matchSetupMp.isDefined) {
        difficult.foreach(diff => matchSetupMp.get.difficulty = diff)
        level.foreach(lvl => matchSetupMp.get.level = lvl)

        this.matchSetupMp.get.setPlayerStatus(serverActor.get, this.multiplayerStatus)
        this.serverActor.get
          .tell(LobbyDataActorMessage(LobbyData(difficulty = Some(matchSetupMp.get
                                                                    .difficulty), level = Some(matchSetupMp.get
                                                                                                 .level), readyPlayers = this
            .matchSetupMp.get.getPlayerStatus)), this.serverActor
                  .get)
        observers
          .foreach(observer => observer
            .notify(LobbyDataEvent(LobbyData(readyPlayers = this.matchSetupMp.get.getPlayerStatus))))
      } else {
        this.clientActor.get ! ReadyActorMessage(this.multiplayerStatus)
      }
    }

    override def kickSomeone(name: String): Unit = {

      this.serverActor.get ! KickActorMessage(this.matchSetupMp.get.removePlayer(name))
      observers
        .foreach(observer => observer
          .notify(LobbyDataEvent(LobbyData(readyPlayers = this.matchSetupMp.get.getPlayerStatus))))
    }
    /*Used by Client Actor*/
    override def gameStart(): Unit = {

      observers
        .foreach(observer => observer
          .notify(LoadLevelEvent()))
    }
    override def gameEnd(): Unit = {

    }
    override def getLobbyData: DataType.LobbyData = LobbyData(Some(
      matchSetupMp.get.difficulty), Some(matchSetupMp.get.level), Some(matchSetupMp.get.mode), matchSetupMp.get
                                                                .getPlayerStatus)

    override def tryJoinLobby(ip: String, port: String): Unit = {
      shutdownMultiplayer()
      this.clientActor = Some(system.actorOf(ClientActor.props(this), "Client"))
      this.clientActor.get ! TryJoin(s"akka://MotoSystem@$ip:$port/user/Server*", this.actualSettings.name)
    }
    override def becomeHost(): Unit = {
      shutdownMultiplayer()
      loadAllLevels()
      serverActor = Some(system.actorOf(ServerActor.props(this), "Server"))
      matchSetupMp = Some(MultiPlayerSetup(mode = true, numPlayers = maxPlayers))
      matchSetupMp.get.tryAddPlayer(serverActor.get, this.actualSettings.name)
      observers
        .foreach(observer => observer
          .notify(SetupLobbyEvent(NetworkAddr.getLocalIPAddress, system.asInstanceOf[ExtendedActorSystem].provider
            .getDefaultAddress.port.get.toString, this.actualSettings.name, levels.map(_.index), difficultiesList
                                    .map(_.number))))
    }
    override def loadAllLevels(): Unit = {
      levels = dataManager.loadLvl()
      observers.foreach(o => o.notify(LevelDataEvent(levels)))
    }
    override def joinResult(result: Boolean): Unit = {
      if (!result) {
        this.shutdownMultiplayer()
      }
      this.observers.foreach(obs => {

        obs.notify(JoinResultEvent(result))
      })
    }
    override def sendToLobbyStrategy[T](strategy: MultiPlayerSetup => T): T = {
      strategy.apply(this.matchSetupMp.get)
    }
    override def sendToViewStrategy(strategy: ObserverUI => Unit): Unit = {
      observers.foreach(o => strategy.apply(o))
    }
    override def gotKicked(): Unit = {
      this.observers.foreach(obs => {
        obs.notify(LeaveLobbyEvent())
        Platform.runLater(() => showNotificationPopup("Sorry, i hate you", "You have been kicked", JavafxEnums
          .SHORT_DURATION, JavafxEnums.INFO_NOTIFICATION, () => _))
        this.shutdownMultiplayer()
      })
    }
    override def shutdownMultiplayer(): Unit = {
      multiplayerStatus = false
      if (this.serverActor.isDefined) {
        this.system.stop(this.serverActor.get)
      }
      if (this.clientActor.isDefined) {
        this.system.stop(this.clientActor.get)
      }
      if (matchSetupMp.isDefined) matchSetupMp = None
      this.serverActor = None
      this.clientActor = None
    }
    override def leaveLobby(): Unit = {
      if (this.serverActor.isDefined) {
        this.serverActor.get ! CloseLobbyActorMessage()
      } else if (this.clientActor.isDefined) {
        this.clientActor.get ! LeaveEvent(this.clientActor.get)
      }
    }
    override def getMediator: Mediator = this.mediator
    override def startMultiplayer(): Unit = {
      serverActor.get ! GameStartActorMessage()
      setupGame(matchSetupMp.get.level)
    }
    override def setupGame(level: Level): Unit = {
      logger info s"level selected: $level"

      val lvl = levels.filter(_.index == level).head
      var playerNum = 1
      val players = if (serverActor.isDefined) {
        playerNum = matchSetupMp.get.numReadyPlayers()
        (0 to playerNum).map(_ => BumperCarEntity(UUID.randomUUID())).toList
      } else {
        List(BumperCarEntity(UUID.randomUUID()))
      }
      val entitiesToRemove = lvl.entities.filter(_.isInstanceOf[Level.Player]).slice(playerNum, maxPlayers)
      lvl.entities = lvl.entities.filterNot(l => entitiesToRemove.contains(l))

      engine = Option(motoscala.engine.GameEngine(this, players, if (matchSetupMp.isDefined) matchSetupMp.get
        .difficulty else actualSettings.diff))
      engine.get.init(lvl)

      var setups: List[LevelSetupData] = List()
      players.slice(1, players.size)
        .foreach(player => setups = setups.:+(LevelSetupData(lvl, isSinglePlayer = false, isHosting = false, player)))

      observers
        .foreach(_.notify(LevelSetupEvent(LevelSetupData(lvl, isSinglePlayer = serverActor
          .isEmpty, isHosting = serverActor.isDefined, players.head))))

      if (serverActor.isDefined) serverActor.get ! SetupsForClientsMessage(setups)
    }
    override def updateScore(value: Option[Int] = None, gameIsEnded: Boolean = false): Int = {
      score += value.getOrElse(0)
      if (gameIsEnded && serverActor.isEmpty && clientActor.isEmpty) {
        var loadedStats = this.dataManager.loadScore().getOrElse(ScoresData(HashMap())).scoreTable
        if (loadedStats.getOrElse(actualSettings.name, 0) < score) {
          loadedStats = loadedStats.updated(actualSettings.name, score)
        }
        this.saveStats(ScoresData(loadedStats))
      }
      if (gameIsEnded) {
        shutdownMultiplayer()
      }
      score
    }
    override def saveStats(newScore: ScoresData): Unit = {
      this.dataManager.saveScore(newScore)
    }
    override def loadStats(): Unit = observers
      .foreach(observer => observer.notify(ScoreDataEvent(this.dataManager.loadScore()
                                                            .getOrElse(ScoresData(HashMap())))))
    override def saveSettings(newSettings: SettingsData): Unit = {
      this.actualSettings = newSettings
      this.dataManager.saveSettings(this.actualSettings)
      this.setAudioVolume(this.actualSettings.musicVolume, actualSettings.effectVolume)
    }
    private def setAudioVolume(musicVolume: Double, effect: Double): Unit = {
      this.soundAgent.enqueueEvent(SetVolumeMusic(musicVolume))
      this.soundAgent.enqueueEvent(SetVolumeEffect(effect))
    }
    private def loadSettings(): SettingsData = this.dataManager.loadSettings().getOrElse(SettingsData())
  }
}


