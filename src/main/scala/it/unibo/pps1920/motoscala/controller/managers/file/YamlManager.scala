package it.unibo.pps1920.motoscala.controller.managers.file

import java.nio.file.Path

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.slf4j.LoggerFactory

import scala.util.Try

class YamlManager {
  private final val logger = LoggerFactory getLogger classOf[YamlManager]
  private val mapper = new ObjectMapper(new YAMLFactory())
  def loadYaml[T](location: Path)(cl: Class[T]): Option[T] = {
    mapper.findAndRegisterModules
    val cls = manifest.runtimeClass.asInstanceOf[Class[T]]
    Try(mapper.readValue(location.toFile, cl)).fold(err => {logger.warn(err.getMessage); None }, Some(_))
  }

  def saveYaml[T](location: Path)(data: T): Boolean = Try(mapper.writeValue(location.toFile, data))
    .fold(err => {logger.warn(err.getMessage); false }, _ => true)

}
