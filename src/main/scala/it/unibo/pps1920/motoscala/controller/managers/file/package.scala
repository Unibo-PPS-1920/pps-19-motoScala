package it.unibo.pps1920.motoscala.controller.managers

import java.io.File
import java.nio.file.{Path, Paths}

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants._

import scala.util.Try


package object file {


  final implicit def stringPathToPath(path: String): Path = Paths.get(path)
  final implicit def pathToFile(path: Path): File = path.toFile
  final def tryAndBoolResult[T](operation: => T)(logger: org.slf4j.Logger): Boolean = Try(operation)
    .fold(error => {logger.warn(error.getMessage); false }, _ => true)

  object FileConstants {
    final val LevelNumber = 2
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
    final val UserCustomLvl: String = AppScoreFolder + SystemSeparator + "lvl.yaml"

  }
}
