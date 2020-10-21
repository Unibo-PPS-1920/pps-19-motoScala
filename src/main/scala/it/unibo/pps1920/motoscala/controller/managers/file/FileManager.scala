package it.unibo.pps1920.motoscala.controller.managers.file

import java.net.URL
import java.nio.file.Path

import org.slf4j.LoggerFactory

/** Provides some useful function for file manipulation inside and outside the jar. */
object FileManager {

  private final val logger = LoggerFactory getLogger getClass
  /** Creates the directory named by this abstract pathname starting from the file parent,
   * including any necessary but nonexistent parent directories.
   *
   * @param path the path representing the file
   * @return false if the operation fails, true otherwise
   * */
  final def createLocalDirectoryTree(path: Path): Boolean = tryAndBoolResult(path.getParentFile.mkdirs())(this.logger)
  /** Creates the directory named by this abstract pathname,
   * including any necessary but nonexistent parent directories.
   *
   * @param path the path representing the file
   * @return false if the operation fails, true otherwise
   * */
  final def createLocalDirectoryTreeFromFile(path: Path): Boolean = tryAndBoolResult(path.mkdirs())(this.logger)
  /** Create one local directory from path.
   *
   * @param path the path representing the directory file
   * @return false if the operation fails, true otherwise
   * */
  final def createLocalDirectory(path: Path): Boolean = tryAndBoolResult(path.mkdir)(this.logger)
  /** Create one Black file.
   *
   * @param path the path representing the file
   * @return false if the operation fails, true otherwise
   * */
  final def createLocalFile(path: Path): Boolean = tryAndBoolResult(path.createNewFile())(this.logger)
  /** Delete one file.
   *
   * @param path the path representing the file
   * @return false if the operation fails, true otherwise
   * */
  final def deleteLocalFile(path: Path): Boolean = tryAndBoolResult(path.delete())(this.logger)
  /** Return the list of file present in one folder.
   *
   * @param path the path representing the directory file
   * @return return one [[List[String]]] each [[String]] represent one file path.
   * */
  final def getListFiles(path: Path): List[String] = path.list((file, _) => file.isFile).toList
  /** Search one resource inside jar, if the jar is present otherwise inside the FS.
   *
   * @param position the path representing the file
   * @return return the file position URL in string format
   * */
  final def loadFromJarToString(position: String): String = loadFromJarToURL(position).toString
  /** Search one resource inside jar, if the jar is present otherwise inside the FS.
   *
   * @param position the path representing the file
   * @return return the file position URL
   * */
  final def loadFromJarToURL(position: String): URL = this.getClass.getResource(position)
}





