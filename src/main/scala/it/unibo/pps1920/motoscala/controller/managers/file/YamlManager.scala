package it.unibo.pps1920.motoscala.controller.managers.file

import java.net.URL
import java.nio.file.Path

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.slf4j.LoggerFactory

import scala.util.Try

/** A class that perform serialization and deserialization on Yaml file. */
private[file] final class YamlManager {
  private final val logger = LoggerFactory getLogger classOf[YamlManager]
  private final val mapper = new ObjectMapper(new YAMLFactory())
  /** Deserialize one yaml file into one Option of T.
   *
   * @param location the path of the file
   * @return one [[Option]] filled whit T instance.
   * */
  def loadYamlFromPath[T](location: Path)(cl: Class[T]): Option[T] = {
    loadResourceFromJar(location.toString.toUri.toURL)(cl)

  }
  private def loadResourceFromJar[T](location: URL)(cl: Class[T]): Option[T] = {
    this.initializeMapper()
    Try(mapper.readValue(location, cl)).fold(err => {logger.warn(err.getMessage); None }, Some(_))
  }
  /** Deserialize one yaml file into one Option of T.
   *
   * @param location the [[URL]] of the file
   * @return one [[Option]] filled whit T instance.
   * */
  def loadYamlFromURL[T](location: URL)(cl: Class[T]): Option[T] = {
    loadResourceFromJar(location)(cl)
  }
  /** Serialize one T instance to one yaml file.
   *
   * @param location the [[Path]] of the file where the instance is serialized.
   * @return True if the serialization returns no error.
   * */
  def saveYaml[T](location: Path)(data: T): Boolean = {
    this.initializeMapper()
    Try(mapper.writeValue(location, data))
      .fold(err => {logger.warn(err.getMessage); false }, _ => true)
  }
  private def initializeMapper(): Unit = {
    import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
    mapper.findAndRegisterModules
    mapper.registerModule(DefaultScalaModule)
    val ptv = BasicPolymorphicTypeValidator.builder.allowIfBaseType(classOf[Any]).build
    mapper.activateDefaultTyping(ptv)
  }

}
