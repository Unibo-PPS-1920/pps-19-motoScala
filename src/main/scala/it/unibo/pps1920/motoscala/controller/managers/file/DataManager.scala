package it.unibo.pps1920.motoscala.controller.managers.file

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants._
import it.unibo.pps1920.motoscala.controller.managers.file.ResourcesJarPaths.Levels
import it.unibo.pps1920.motoscala.controller.managers.file.ResourcesPaths.{ScoreFile, SettingsFile}
import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.model.Scores.ScoresData
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
import org.slf4j.LoggerFactory

final class DataManager {

  private final val logger = LoggerFactory getLogger classOf[DataManager]
  private val yamlManager: YamlManager = new YamlManager
  def loadSettings(): Option[SettingsData] = yamlManager.loadYamlFromPath(SettingsFile)(classOf[SettingsData])
  def saveSettings(sett: SettingsData): Boolean = yamlManager.saveYaml(SettingsFile)(sett)
  def loadScore(): Option[ScoresData] = yamlManager.loadYamlFromPath(ScoreFile)(classOf[ScoresData])
  def saveScore(scores: ScoresData): Boolean = yamlManager.saveYaml(ScoreFile)(scores)
  def initAppDirectory(): Boolean = {
    tryAndBoolResult(FileManager.createLocalDirectoryTreeFromFile(AppMainFolder),
                     FileManager.createLocalDirectory(AppSettingsFolder),
                     FileManager.createLocalDirectory(AppScoreFolder),
                     FileManager.createLocalDirectory(AppUserLevelFolder))((this.logger))

  }
  def loadUserLvl(): List[LevelData] = {
    FileManager.getListFiles(AppUserLevelFolder)
      .map(pf => this.yamlManager.loadYamlFromPath(AppUserLevelFolder + pf)(classOf[LevelData]))
      .filter(_.isDefined).map(_.get)
  }
  def saveLvl(data: LevelData): Unit = {
    this.yamlManager.saveYaml(AppMainFolder + SystemSeparator + "lvl1.yaml")(data)
  }
  def loadLvl(): List[LevelData] = {

    (1 to LevelNumber).map(i => {
      yamlManager
        .loadYamlFromURL(FileManager.loadFromJarToURL(s"${Levels}lvl${i}.yaml"))(classOf[LevelData]).get
    }).toList
  }

}
