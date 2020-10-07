package it.unibo.pps1920.motoscala.controller

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import it.unibo.pps1920.motoscala.controller.mediation.Mediator
import it.unibo.pps1920.motoscala.engine.Engine
import it.unibo.pps1920.motoscala.actors.ClientActor
import akka.actor.ActorRef

class ConcreteActorController(protected val mediator: Mediator){

  override def notifySettingsUpdate(): Unit = {}
  override def notifyUsersUpdate(): Unit = {}
  override def notifyGameStart(): Unit = {}
  override def notifyGameEnd(): Unit = {}
  override def notifyReady(): Unit = {}
  override def getMediator(): Unit = {}
  def clientMode(): Unit ={

    clientActor = Some(system.actorOf(ClientActor.props(mediator, this)))
  }
  def serverMode(gameEngine : Engine) ={}
  private val config = ConfigFactory.load("application")
  private val system = ActorSystem("System", config)
  private var clientActor : Option[ActorRef] = None
  private var serverActor : Option[ActorRef] = None


}
