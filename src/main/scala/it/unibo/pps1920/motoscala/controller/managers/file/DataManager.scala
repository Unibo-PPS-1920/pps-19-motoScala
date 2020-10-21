package it.unibo.pps1920.motoscala.controller.managers.file

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants._
import it.unibo.pps1920.motoscala.controller.managers.file.FileExtension.Yaml
import it.unibo.pps1920.motoscala.controller.managers.file.FileName.LevelFile
import it.unibo.pps1920.motoscala.controller.managers.file.ResourcesJarPaths.Levels
import it.unibo.pps1920.motoscala.controller.managers.file.ResourcesPaths.{ScoreFile, SettingsFile}
import it.unibo.pps1920.motoscala.model.Level.LevelData
import it.unibo.pps1920.motoscala.model.Scores.ScoresData
import it.unibo.pps1920.motoscala.model.Settings.SettingsData
import org.slf4j.LoggerFactory


/** A class that manage the Yaml file, handling settings, scores and levels */
final class DataManager {

  private final val logger = LoggerFactory getLogger classOf[DataManager]
  private val yamlManager: YamlManager = new YamlManager
  /** Deserialize and load then return the settings file.
   *
   * @return one SettingsData object.
   * */
  def loadSettings(): Option[SettingsData] = yamlManager.loadYamlFromPath(SettingsFile)(classOf[SettingsData])
  /** Serialize the SettingsData passed to standard settings folder.
   *
   * @param sett The SettingsData that is serialized
   * @return True if the serialization returns no error.
   */
  def saveSettings(sett: SettingsData): Boolean = yamlManager.saveYaml(SettingsFile)(sett)
  /** Deserialize and load then return the stats file.
   *
   * @return one ScoresData object.
   */
  def loadScore(): Option[ScoresData] = yamlManager.loadYamlFromPath(ScoreFile)(classOf[ScoresData])
  /** Serialize the ScoresData passed to standard stats folder.
   *
   * @param scores The scores table.
   * @return True if the serialization returns no error.
   */
  def saveScore(scores: ScoresData): Boolean = yamlManager.saveYaml(ScoreFile)(scores)
  /** Initializes the main application tree folder.
   *
   * @return True if the operations returns no error.
   */
  def initAppDirectory(): Boolean = {
    tryAndBoolResult(FileManager.createLocalDirectoryTreeFromFile(AppMainFolder),
                     FileManager.createLocalDirectory(AppSettingsFolder),
                     FileManager.createLocalDirectory(AppScoreFolder),
                     FileManager.createLocalDirectory(AppUserLevelFolder))((this.logger))
  }
  /** Deserialize and return one list custom user level, located inside the user level folder.
   *
   * @return the List[LevelData.
   */
  def loadUserLvl(): List[LevelData] = {
    FileManager.getListFiles(AppUserLevelFolder)
      .map(pf => this.yamlManager.loadYamlFromPath(AppUserLevelFolder + pf)(classOf[LevelData]))
      .filter(_.isDefined).map(_.get)
  }
  /** Serialize one custom user level. */
  def saveLvl(data: LevelData): Unit = {
    this.yamlManager.saveYaml(s"${AppMainFolder}${SystemSeparator}${LevelFile}${Yaml}")(data)
  }

  /** Deserialize internal application levels.
   *
   * @return the List[LevelData.
   */
  def loadLvl(): List[LevelData] = {
    (1 to LevelNumber).map(i => {
      yamlManager
        .loadYamlFromURL(FileManager.loadFromJarToURL(s"${Levels}${LevelFile}${i}${Yaml}"))(classOf[LevelData]).get
    }).toList
  }
}
