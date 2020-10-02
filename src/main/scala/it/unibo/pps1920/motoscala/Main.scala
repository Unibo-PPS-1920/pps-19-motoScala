package it.unibo.pps1920.motoscala

import java.nio.file.Paths

import it.unibo.pps1920.motoscala.controller.Controller
import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants._
import it.unibo.pps1920.motoscala.controller.managers.file.YamlManager
import it.unibo.pps1920.motoscala.ecs.components.Shape.{Circle, Rectangle}
import it.unibo.pps1920.motoscala.ecs.util.Vector2
import it.unibo.pps1920.motoscala.model.Level._
import it.unibo.pps1920.motoscala.view.View

object Main extends App {
  val controller = Controller()
  val view = View(controller)
  view.start()

  val yamlManager: YamlManager = new YamlManager()


  yamlManager.saveYaml(Paths
                         .get(APP_USER_LEVEL_FOLDER + SYSTEM_SEPARATOR + "Lvl5.yaml"))(
    new LevelData(1, Coordinate(100, 100), List(Tile(Rectangle(80, 80), Coordinate(50, 50), tangible = true),
                                                Tile(Rectangle(20, 100), Coordinate(10, 50), tangible = false), Tile(Rectangle(20, 100), Coordinate(90, 50), tangible = false), Tile(Rectangle(80, 20), Coordinate(50, 10), tangible = false),
                                                Tile(Rectangle(80, 20), Coordinate(50, 90), tangible = false), Player(Coordinate(1, 1), Circle(5), Coordinate(50, 50), 3))))

  println(yamlManager.loadYaml(Paths
                                 .get(APP_USER_LEVEL_FOLDER + SYSTEM_SEPARATOR + "Lvl5.yaml"))(classOf[LevelData]))

  yamlManager.saveYaml(Paths
                         .get(APP_USER_LEVEL_FOLDER + SYSTEM_SEPARATOR + "Lvl7.yaml"))(new LevelData12(1, Vector2
    .apply(1, 2), List(Enemy1(Coordinate(1, 2), Rectangle(1, 2), Coordinate(1, 2), 3), Player(Coordinate(1, 2), Circle(12), Coordinate(1, 2), 3), Tile(Rectangle(4, 5), Coordinate(6, 7), tangible = true))))


  println(yamlManager.loadYaml(Paths
                                 .get(APP_USER_LEVEL_FOLDER + SYSTEM_SEPARATOR + "Lvl7.yaml"))(classOf[LevelData12]))

}
