package it.unibo.pps1920.motoscala.controller.managers

import java.io.File
import java.nio.file.{Path, Paths}

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants._

import scala.util.Try

/** One utility package object that contains most of the [[DataManager]] constants path, and some utility.
 * It provides some implicit for:
 * [[String]] to [[java.nio.file.Path]] conversion
 * and
 * [[Path]] to [[File]] conversion
 *
 * *  {{{
 *  *  val x = "/Usr/bin/pippo.exe"
 *  *  x.canExecute
 *  *  }}}
 *
 * */
package object file {
  /** Converts one [[String]] to one [[Path]]
   *
   * @param path the [[String]] that to be converted
   * @return the [[Path]] from [[String]]
   * */
  final implicit def stringPathToPath(path: String): Path = Paths.get(path)
  /** Converts one [[Path]] to one [[File]]
   *
   * @param path the [[Path]] that to be converted
   * @return the [[File]] from path
   * */
  final implicit def pathToFile(path: Path): File = path.toFile
  /** Try some operation and return true if no one exception is encountered
   *
   * @param operation the operation
   * @return false if fails, true otherwise
   */
  final def tryAndBoolResult[T](operation: => T)(logger: org.slf4j.Logger): Boolean = Try(operation)
    .fold(error => {logger.warn(error.getMessage); false }, _ => true)

  object FileConstants {
    final val LevelNumber = 3
    final val SystemSeparator: String = File.separator
    final val UserHome: String = System.getProperty("user.home")
    final val AppMainFolder: String = UserHome + SystemSeparator + "MotoScala"
    final val AppSettingsFolder: String = AppMainFolder + SystemSeparator + "Settings"
    final val AppScoreFolder: String = AppMainFolder + SystemSeparator + "Score"
    final val AppUserLevelFolder: String = AppMainFolder + SystemSeparator + "UserLevels"
  }

  object ResourcesPaths {
    final val ScoreFile: String = AppScoreFolder + SystemSeparator + "Stats.yaml"
    final val SettingsFile: String = AppSettingsFolder + SystemSeparator + "Settings.yaml"
    final val UserCustomLvl: String = AppScoreFolder + SystemSeparator + "Lvl.yaml"
  }

  object ResourcesJarPaths {
    final val Levels: String = "/levels/"
    final val Music: String = "/music/"
    final val Clips: String = "/clips/"
    final val SettingsFile2: String = AppSettingsFolder + SystemSeparator + "Settings.yaml"
    final val UserCustomLvl: String = AppScoreFolder + SystemSeparator + "lvl1.yaml"
  }
}
