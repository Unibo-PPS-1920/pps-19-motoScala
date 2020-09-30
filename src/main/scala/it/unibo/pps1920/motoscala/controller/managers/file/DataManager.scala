package it.unibo.pps1920.motoscala.controller.managers.file

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants.{APP_MAIN_FOLDER, APP_SCORE_FOLDER, APP_SETTINGS_FOLDER, APP_USER_LEVEL_FOLDER}
import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.stringPathToPath
import it.unibo.pps1920.motoscala.model.Level.LevelData

class DataManager {

  private val yamlManager: YamlManager = new YamlManager


  def loadSettings(): Unit = {}
  def saveSettings(): Unit = ???
  def loadScore(): Unit = ???
  def saveScore(): Unit = ???
  def initAppDirectory(): Unit = {
    FileManager.createLocalDirectoryTreeFromFile(APP_MAIN_FOLDER)
    FileManager.createLocalDirectory(APP_SETTINGS_FOLDER)
    FileManager.createLocalDirectory(APP_SCORE_FOLDER)
    FileManager.createLocalDirectory(APP_USER_LEVEL_FOLDER)
  }
  def loadUserLvl(): List[LevelData] = {
    var lis: List[LevelData] = List()
    FileManager.getListFiles(APP_USER_LEVEL_FOLDER).foreach(fileName => {
      this.yamlManager.loadYaml(APP_USER_LEVEL_FOLDER + fileName)(classOf[LevelData]).foreach(lis.::(_))
    })
    lis
  }
  def loadLvl(): Unit = ???
  def saveLvl(): Unit = ???

}
