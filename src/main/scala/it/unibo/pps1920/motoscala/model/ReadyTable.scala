package it.unibo.pps1920.motoscala.model

import akka.actor.ActorRef

import scala.collection.mutable

object ReadyTable {
  sealed trait ReadyTable
  case class ReadyPlayers(list: mutable.Map[ActorRef, PlayerData] = mutable.LinkedHashMap()) extends ReadyTable

}

