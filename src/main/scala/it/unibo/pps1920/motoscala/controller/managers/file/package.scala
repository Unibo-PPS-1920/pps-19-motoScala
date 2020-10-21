package it.unibo.pps1920.motoscala.controller.managers

import java.io.File
import java.nio.file.{Path, Paths}

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants._
import it.unibo.pps1920.motoscala.controller.managers.file.FileExtension.Yaml
import it.unibo.pps1920.motoscala.controller.managers.file.FileName.{LevelFile, StatsFile}
import it.unibo.pps1920.motoscala.controller.managers.file.FolderName.{MainFolder, ScoreFolder, SettingsFolder, UserLevelFolder}

import scala.util.Try

/** One utility package object that contains most of the [[it.unibo.pps1920.motoscala.controller.managers.file.DataManager]] constants path, and some utility.
 * It provides some implicit for:
 * <p>
 * String to [[java.nio.file.Path]] conversion
 * and
 * [[java.nio.file.Path]] to [[java.io.File]] conversion
 *
 * {{{
 *  val x = "/Usr/bin/pippo.exe"
 *  x.canExecute
 * }}}
 *
 * */
package object file {
  /** Converts one String to one Path.
   *
   * @param path the String that to be converted
   * @return the Path from String
   * */
  final implicit def stringPathToPath(path: String): Path = Paths.get(path)
  /** Converts one Path to one File.
   *
   * @param path the Path that to be converted
   * @return the File from path
   * */
  final implicit def pathToFile(path: Path): File = path.toFile
  /** Try some operation and return true if no one exception is encountered.
   *
   * @param operation the operation
   * @return false if fails, true otherwise
   */
  final def tryAndBoolResult[T](operation: => T)(logger: org.slf4j.Logger): Boolean =
    Try(operation).fold(error => {logger.warn(error.getMessage); false }, _ => true)

  object FileConstants {
    final val LevelNumber = 3
    final val SystemSeparator: String = File.separator
    final val UserHome: String = System.getProperty("user.home")
    final val AppMainFolder: String = UserHome + SystemSeparator + MainFolder
    final val AppSettingsFolder: String = AppMainFolder + SystemSeparator + SettingsFolder
    final val AppScoreFolder: String = AppMainFolder + SystemSeparator + ScoreFolder
    final val AppUserLevelFolder: String = AppMainFolder + SystemSeparator + UserLevelFolder
  }

  object ResourcesPaths {
    final val ScoreFile: String = AppScoreFolder + SystemSeparator + StatsFile + Yaml
    final val SettingsFile: String = AppSettingsFolder + SystemSeparator + SettingsFile + Yaml
    final val UserCustomLvl: String = AppScoreFolder + SystemSeparator + LevelFile + Yaml
  }

  object ResourcesJarPaths {
    final val Levels: String = "/levels/"
    final val Music: String = "/music/"
    final val Clips: String = "/clips/"
  }

  object FileExtension {
    final val Yaml: String = ".yaml"
  }

  object FolderName {
    final val MainFolder: String = "MotoScala"
    final val SettingsFolder: String = "Settings"
    final val ScoreFolder: String = "Score"
    final val UserLevelFolder: String = "UserLevels"
  }

  object FileName {
    final val StatsFile: String = "Stats"
    final val SettingsFile: String = "Settings"
    final val LevelFile: String = "lvl"
  }


}
