package it.unibo.pps1920.motoscala.engine

trait Engine {

}

class GameEngine private() extends Engine {

}

object GameEngine {

  def apply(): GameEngine = new GameEngine()
}


