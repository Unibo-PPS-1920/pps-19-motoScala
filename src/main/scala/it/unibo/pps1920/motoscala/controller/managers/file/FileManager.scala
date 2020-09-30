package it.unibo.pps1920.motoscala.controller.managers.file

import java.io.File
import java.nio.file.{Path, Paths}

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants.{APP_SCORE_FOLDER, APP_SETTINGS_FOLDER, SYSTEM_SEPARATOR}
import org.slf4j.LoggerFactory

import scala.util.Try

object FileManager {

  private final val logger = LoggerFactory getLogger getClass
  final implicit def stringPathToPath(path: String): Path = Paths.get(path)
  final def createLocalDirectoryTree(path: Path): Boolean = tryAndReturn(path.toFile.getParentFile.mkdirs())
  final def createLocalDirectoryTreeFromFile(path: Path): Boolean = tryAndReturn(path.toFile.mkdirs())
  final def createLocalDirectory(path: Path): Boolean = tryAndReturn(path.toFile.mkdir)
  private final def tryAndReturn[T](operation: => T): Boolean = Try(operation)
    .fold(error => {logger.warn(error.getMessage); false }, _ => true)
  final def createLocalFile(path: Path): Boolean = tryAndReturn(path.toFile.createNewFile())
  final def deleteLocalFile(path: Path): Boolean = tryAndReturn(path.toFile.delete())
  final def getListFiles(path: Path): List[String] = path.toFile.list((file, _) => file.isFile).toList
  final def loadLocalFile(): Unit = ???
  final def resolveRelativePath(): Unit = ???
  final def resolveAbsolutePath(): Unit = ???
}

object FileConstants {
  final val SYSTEM_SEPARATOR: String = File.separator
  final val USER_HOME: String = System.getProperty("user.home")
  final val APP_MAIN_FOLDER: String = USER_HOME + SYSTEM_SEPARATOR + "MotoScala"
  final val APP_SETTINGS_FOLDER: String = APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "Settings"
  final val APP_SCORE_FOLDER: String = APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "Score"
  final val APP_USER_LEVEL_FOLDER: String = APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "UserLevels"

}


object ResourcesPaths {
  final val STATS_FILE: String = APP_SCORE_FOLDER + SYSTEM_SEPARATOR + "Stats.yml"
  final val SETTINGS_FILE: String = APP_SETTINGS_FOLDER + SYSTEM_SEPARATOR + "Stats.yml"
}



