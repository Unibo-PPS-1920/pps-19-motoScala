package it.unibo.pps1920.motoscala.controller.managers.file

import java.nio.file.Path

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

class YamlManager {
  private val mapper = new ObjectMapper(new YAMLFactory())
  def loadYaml[T](location: Path): T = {
    mapper.findAndRegisterModules
    val result = mapper.readValue(location.toFile, classOf[T])
    result
  }
  def saveYaml[T](location: Path)(data: T): Unit = {
    mapper.writeValue(location.toFile, data)
  }
}
