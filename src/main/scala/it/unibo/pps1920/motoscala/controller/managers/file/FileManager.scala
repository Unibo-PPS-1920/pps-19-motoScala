package it.unibo.pps1920.motoscala.controller.managers.file

import java.io.File
import java.nio.file.{Path, Paths}

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants.{APP_SCORE_FOLDER, APP_SETTINGS_FOLDER, SYSTEM_DIVIDER}
import org.slf4j.LoggerFactory

import scala.util.Try

object FileManager {

  private final val logger = LoggerFactory getLogger getClass
  final implicit def stringPathToPath(path: String): Path = Paths.get(path)
  final def createLocalDirectoryTree(path: Path)(): Boolean = Try(path.toFile.mkdirs())
    .fold(error => {logger.warn(error.getMessage); false }, _ => true)
  final def createLocalDirectory(path: Path): Boolean = Try(path.toFile.mkdir)
    .fold(error => {logger.warn(error.getMessage); false }, _ => true)
  final def createLocalFile(path: String): Boolean = ???
  final def deleteLocalFile(path: String): Boolean = ???
  final def loadLocalFile(): Unit = ???
  final def resolveRelativePath(): Unit = ???
  final def resolveAbsolutePath(): Unit = ???
}

object FileConstants {
  final val SYSTEM_DIVIDER: String = File.separator
  final val USER_HOME: String = System.getProperty("user.home")
  final val APP_MAIN_FOLDER: String = USER_HOME + "MotoScala"
  final val APP_SETTINGS_FOLDER: String = USER_HOME + SYSTEM_DIVIDER + "Settings"
  final val APP_SCORE_FOLDER: String = USER_HOME + SYSTEM_DIVIDER + "Score"
}


object ResourcesPaths {
  final val STATS_FILE: String = APP_SCORE_FOLDER + SYSTEM_DIVIDER + "Stats.yml"
  final val SETTINGS_FILE: String = APP_SETTINGS_FOLDER + SYSTEM_DIVIDER + "Stats.yml"
}



