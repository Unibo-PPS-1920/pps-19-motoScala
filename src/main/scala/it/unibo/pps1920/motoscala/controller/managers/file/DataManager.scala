package it.unibo.pps1920.motoscala.controller.managers.file

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants._
import it.unibo.pps1920.motoscala.controller.managers.file.ResourcesPaths.{SCORE_FILE, SETTINGS_FILE}
import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.model.Scores.ScoresData
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
import org.slf4j.LoggerFactory

final class DataManager {

  private final val logger = LoggerFactory getLogger classOf[DataManager]
  private val yamlManager: YamlManager = new YamlManager
  def loadSettings(): Option[SettingsData] = yamlManager.loadYaml(SETTINGS_FILE)(classOf[SettingsData])
  def saveSettings(sett: SettingsData): Boolean = yamlManager.saveYaml(SETTINGS_FILE)(sett)
  def loadScore(): Option[ScoresData] = yamlManager.loadYaml(SCORE_FILE)(classOf[ScoresData])
  def saveScore(scores: ScoresData): Boolean = yamlManager.saveYaml(SCORE_FILE)(scores)
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
  def saveLvl(data: LevelData): Unit = {
    this.yamlManager.saveYaml(APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "lvl.yaml")(data)
  }
  def loadLvl(): List[LevelData] = ???

}
