package it.unibo.pps1920.motoscala.controller.managers.file

import java.net.URL
import java.nio.file.Path

import org.slf4j.LoggerFactory

object FileManager {

  private final val logger = LoggerFactory getLogger getClass
  final def createLocalDirectoryTree(path: Path): Boolean = tryAndBoolResult(path.getParentFile.mkdirs())(this
                                                                                                            .logger)
  final def createLocalDirectoryTreeFromFile(path: Path): Boolean = tryAndBoolResult(path.mkdirs())(this.logger)
  final def createLocalDirectory(path: Path): Boolean = tryAndBoolResult(path.mkdir)(this.logger)
  final def createLocalFile(path: Path): Boolean = tryAndBoolResult(path.createNewFile())(this.logger)
  final def deleteLocalFile(path: Path): Boolean = tryAndBoolResult(path.delete())(this.logger)
  final def getListFiles(path: Path): List[String] = path.list((file, _) => file.isFile).toList
  final def loadFromJarToString(position: String): String = {
    loadFromJarToURL(position).toString
  }

  final def loadFromJarToURL(position: String): URL = {
    this.getClass.getResource(position)
  }
}





