package it.unibo.pps1920.motoscala.model

import scala.collection.immutable.HashMap

object ReadyTable {
  sealed trait ReadyTable
  case class ReadyPlayers(list: HashMap[String, Boolean] = HashMap()) extends ReadyTable

}

