package it.unibo.pps1920.motoscala.controller.managers.file

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants.{APP_MAIN_FOLDER, APP_SCORE_FOLDER, APP_SETTINGS_FOLDER, APP_USER_LEVEL_FOLDER}
import it.unibo.pps1920.motoscala.controller.managers.file.ResourcesPaths.{SCORE_FILE, SETTINGS_FILE}
import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.model.Scores.Score
import it.unibo.pps1920.motoscala.model.Settings.Setting
import org.slf4j.LoggerFactory

final class DataManager {

  private final val logger = LoggerFactory getLogger classOf[DataManager]
  private val yamlManager: YamlManager = new YamlManager
  def loadSettings(): Option[Setting] = yamlManager.loadYaml(SETTINGS_FILE)(classOf[Setting])
  def saveSettings(sett: Setting): Boolean = yamlManager.saveYaml(SETTINGS_FILE)(sett)
  def loadScore(): Option[Score] = yamlManager.loadYaml(SCORE_FILE)(classOf[Score])
  def saveScore(scores: Score): Boolean = yamlManager.saveYaml(SCORE_FILE)(scores)
  def initAppDirectory(): Boolean = {
    tryAndBoolResult(FileManager.createLocalDirectoryTreeFromFile(APP_MAIN_FOLDER),
                     FileManager.createLocalDirectory(APP_SETTINGS_FOLDER),
                     FileManager.createLocalDirectory(APP_SCORE_FOLDER),
                     FileManager.createLocalDirectory(APP_USER_LEVEL_FOLDER))((this.logger))

  }
  def loadUserLvl(): List[LevelData] = {
    FileManager.getListFiles(APP_USER_LEVEL_FOLDER)
      .map(pf => this.yamlManager.loadYaml(APP_USER_LEVEL_FOLDER + pf)(classOf[LevelData]))
      .filter(_.isDefined).map(_.get)
  }

  def loadLvl(): List[LevelData] = ???

}
