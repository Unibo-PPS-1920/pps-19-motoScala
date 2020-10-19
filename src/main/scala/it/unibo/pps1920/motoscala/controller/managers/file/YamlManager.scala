package it.unibo.pps1920.motoscala.controller.managers.file

import java.net.URL
import java.nio.file.Path

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.slf4j.LoggerFactory

import scala.util.Try

final class YamlManager {
  private final val logger = LoggerFactory getLogger classOf[YamlManager]
  private final val mapper = new ObjectMapper(new YAMLFactory())
  def loadYaml[T](location: Path)(cl: Class[T]): Option[T] = {
    this.initializeMapper()
    Try(mapper.readValue(location, cl)).fold(err => {logger.warn(err.getMessage); None }, Some(_))
  }

  def loadYamlFromURL[T](location: URL)(cl: Class[T]): Option[T] = {
    this.initializeMapper()
    Try(mapper.readValue(location, cl)).fold(err => {logger.warn(err.getMessage); None }, Some(_))
  }
  private def initializeMapper(): Unit = {
    import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
    mapper.findAndRegisterModules
    mapper.registerModule(DefaultScalaModule)
    val ptv = BasicPolymorphicTypeValidator.builder.allowIfBaseType(classOf[Any]).build
    mapper.activateDefaultTyping(ptv)
  }
  def saveYaml[T](location: Path)(data: T): Boolean = {
    this.initializeMapper()
    Try(mapper.writeValue(location, data))
      .fold(err => {logger.warn(err.getMessage); false }, _ => true)
  }

}
