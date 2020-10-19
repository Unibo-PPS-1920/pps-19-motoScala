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
    final val SYSTEM_SEPARATOR: String = File.separator
    final val USER_HOME: String = System.getProperty("user.home")
    final val APP_MAIN_FOLDER: String = USER_HOME + SYSTEM_SEPARATOR + "MotoScala"
    final val APP_SETTINGS_FOLDER: String = APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "Settings"
    final val APP_SCORE_FOLDER: String = APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "Score"
    final val APP_USER_LEVEL_FOLDER: String = APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "UserLevels"

  }

  object ResourcesPaths {
    final val SCORE_FILE: String = APP_SCORE_FOLDER + SYSTEM_SEPARATOR + "Stats.yml"
    final val SETTINGS_FILE: String = APP_SETTINGS_FOLDER + SYSTEM_SEPARATOR + "Settings.yml"
    final val USER_LVL: String = APP_SCORE_FOLDER + SYSTEM_SEPARATOR + "Lvl.yml"

  }
}
