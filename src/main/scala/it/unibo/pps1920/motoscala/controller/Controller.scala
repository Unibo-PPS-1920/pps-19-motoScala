package it.unibo.pps1920.motoscala.controller

import java.util.UUID
import java.util.UUID.randomUUID

import akka.actor.{ActorRef, ActorSystem, ExtendedActorSystem}
import com.typesafe.config.ConfigFactory
import it.unibo.pps1920.motoscala
import it.unibo.pps1920.motoscala.controller.managers.audio.MediaEvent.{SetVolumeEffect, SetVolumeMusic}
import it.unibo.pps1920.motoscala.controller.managers.audio.{MediaEvent, SoundAgent}
import it.unibo.pps1920.motoscala.controller.managers.file.DataManager
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.engine.Engine
import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.model.Scores.ScoresData
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
import it.unibo.pps1920.motoscala.model.{MultiPlayerSetup, NetworkAddr, SinglePlayerSetup}
import it.unibo.pps1920.motoscala.multiplayer.actors.{ClientActor, ServerActor}
import it.unibo.pps1920.motoscala.multiplayer.messages.ActorMessage._
import it.unibo.pps1920.motoscala.multiplayer.messages.DataType
import it.unibo.pps1920.motoscala.multiplayer.messages.MessageData.LobbyData
import it.unibo.pps1920.motoscala.view.events.ViewEvent._
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
    private val myUuid: UUID = randomUUID()
    private val dataManager: DataManager = new DataManager()
    //campi che arrivano dal fu ConcreteActorController
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
    private var matchSetupSp: Option[SinglePlayerSetup] = None
    private var status: Boolean = false
    this.soundAgent.start()

    this.setAudioVolume(this.actualSettings.volume)


    override def attachUI(obs: ObserverUI*): Unit = observers = observers ++ obs
    override def detachUI(obs: ObserverUI*): Unit = observers = observers -- obs
    override def setupGame(level: Level): Unit = {
      logger info s"level selected: $level"
      engine = Option(motoscala.engine.GameEngine(this, myUuid))
      engine.get.init(levels.filter(data => data.index == level).head)
    }

    override def start(): Unit = {
      score = 0
      engine.get.start()
    }
    override def loadAllLevels(): Unit = {
      /*      levels = List(
              LevelData(0, Coordinate(ViewConstants.Canvas.CanvasWidth, ViewConstants.Canvas.CanvasHeight),
                        List(Level.Player(Coordinate(500, 500), Circle(25), Coordinate(0, 0), Coordinate(15, 15)),
                             Level.RedPupa(Coordinate(600, 500), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                             Level.BlackPupa(Coordinate(600, 100), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                             Level.Polar(Coordinate(600, 300), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                             Level.RedPupa(Coordinate(300, 100), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                             Level.RedPupa(Coordinate(600, 200), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                             Level.BlackPupa(Coordinate(700, 700), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                             Level.JumpPowerUp(Coordinate(100, 100), Circle(20)),
                             Level.SpeedBoostPowerUp(Coordinate(200, 200), Circle(20)),
                             Level.WeightBoostPowerUp(Coordinate(300, 300), Circle(20))
                             )))

            this.dataManager
              .saveLvl(LevelData(0, Coordinate(ViewConstants.Canvas.CanvasWidth, ViewConstants.Canvas.CanvasHeight),
                                 List(Level.Player(Coordinate(500, 500), Circle(25), Coordinate(0, 0), Coordinate(15, 15)),
                                      Level.BlackPupa(Coordinate(600, 500), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                                      Level.BlackPupa(Coordinate(600, 100), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                                      Level.BlackPupa(Coordinate(600, 300), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                                      Level.BluePupa(Coordinate(300, 100), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                                      Level.BluePupa(Coordinate(600, 200), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                                      Level.BluePupa(Coordinate(700, 700), Circle(25), Coordinate(0, 0), Coordinate(3, 3)),
                                      Level.WeightBoostPowerUp(Coordinate(300, 300), Circle(20))
                                      )))*/
      levels = dataManager.loadLvl()

      observers.foreach(o => o.notify(LevelDataEvent(levels)))
    }
    override def pause(): Unit = engine.get.pause()
    override def resume(): Unit = engine.get.resume()
    override def stop(): Unit = {
      engine.get.stop()
      engine = None
    }

    override def redirectSoundEvent(me: MediaEvent): Unit = this.soundAgent.enqueueEvent(me)

    override def loadStats(): Unit = observers
      .foreach(observer => observer.notify(ScoreDataEvent(this.dataManager.loadScore()
                                                            .getOrElse(ScoresData(HashMap("GINO" -> 100000, "GINO2" -> 100000))))))
    override def loadSetting(): Unit = observers
      .foreach(observer => observer.notify(SettingsDataEvent(this.actualSettings)))

    override def saveStats(newSettings: SettingsData): Unit = {
      this.actualSettings = newSettings
      this.dataManager.saveSettings(this.actualSettings)
      this.setAudioVolume(this.actualSettings.volume)
    }
    private def setAudioVolume(value: Double): Unit = {
      this.soundAgent.enqueueEvent(SetVolumeMusic(this.actualSettings.volume))
      this.soundAgent.enqueueEvent(SetVolumeEffect(this.actualSettings.volume))
    }
    override def setSelfReady(): Unit = {
      this.status = !this.status
      if (serverActor.isDefined) {
        this.matchSetupMp.get.setPlayerStatus(serverActor.get, this.status)
        this.serverActor.get
          .tell(LobbyDataActorMessage(LobbyData(readyPlayers = this.matchSetupMp.get.readyPlayers)), this.serverActor
            .get)
        observers
          .foreach(observer => observer
            .notify(LobbyDataEvent(LobbyData(readyPlayers = this.matchSetupMp.get.readyPlayers))))
      } else {
        this.clientActor.get ! ReadyActorMessage(this.status)
      }
    }
    override def kickSomeone(name: String): Unit = {
      this.serverActor.get ! KickActorMessage(this.matchSetupMp.get.removePlayer(name))
      observers
        .foreach(observer => observer
          .notify(LobbyDataEvent(LobbyData(readyPlayers = this.matchSetupMp.get.readyPlayers))))
    }
    /*Used by Client Actor*/
    override def gameStart(): Unit = ???
    override def gameEnd(): Unit = {

    }
    override def getLobbyData: DataType.LobbyData = LobbyData(Some(matchSetupMp.get.difficulty), Some(matchSetupMp.get
                                                                                                        .mode), matchSetupMp
                                                                .get.readyPlayers)
    override def tryJoinLobby(ip: String, port: String): Unit = {
      this.clientActor = Some(system.actorOf(ClientActor.props(this), "Client"))
      this.clientActor.get ! TryJoin(s"akka://MotoSystem@$ip:$port/user/Server*", this.actualSettings.name)
    }
    override def becomeHost(): Unit = {
      serverActor = Some(system.actorOf(ServerActor.props(this), "Server"))
      matchSetupMp = Some(MultiPlayerSetup(1, mode = true, 10))
      matchSetupMp.get.tryAddPlayer(serverActor.get, this.actualSettings.name)
      observers
        .foreach(observer => observer
          .notify(SetupLobbyEvent(NetworkAddr.getLocalIPAddress, system.asInstanceOf[ExtendedActorSystem].provider
            .getDefaultAddress.port.get.toString, this.actualSettings.name)))
    }
    override def joinResult(result: Boolean): Unit = {
      if (!result) {
        this.shutdownMultiplayer()
      }
      this.observers.foreach(obs => {
        obs.notify(JoinResultEvent(result))
      })
    }
    override def shutdownMultiplayer(): Unit = {

      if (this.serverActor.isDefined) {
        this.system.stop(this.serverActor.get)
      } else if (this.clientActor.isDefined) {
        this.system.stop(this.clientActor.get)
      }
      this.serverActor = None
      this.clientActor = None
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
          .SHORT_DURATION, JavafxEnums.ERROR_NOTIFICATION, _))
        this.shutdownMultiplayer()
      })
    }
    override def leaveLobby(): Unit = {
      if (this.serverActor.isDefined) {
        this.serverActor.get ! CloseLobbyActorMessage()
      } else if (this.clientActor.isDefined) {
        this.clientActor.get ! LeaveEvent(this.clientActor.get)
      }
    }
    override def getMediator: Mediator = this.mediator
    override def updateScore(delta: Int): Int = {
      score += delta
      score
    }
    private def loadSettings(): SettingsData = this.dataManager.loadSettings().getOrElse(SettingsData())
  }
}


