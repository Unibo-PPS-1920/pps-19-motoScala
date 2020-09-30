package it.unibo.pps1920.motoscala.controller.managers.file

import com.fasterxml.jackson.annotation.{JsonAutoDetect, JsonProperty}

object SerializableData {
  case class dataSettings()
  case class dataScores()


}

case class DataLevel(
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @JsonProperty val xx: Int
)
