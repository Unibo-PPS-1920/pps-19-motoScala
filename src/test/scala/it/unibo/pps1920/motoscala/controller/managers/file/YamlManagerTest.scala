package it.unibo.pps1920.motoscala.controller.managers.file

import java.nio.file.Paths

import com.fasterxml.jackson.annotation.JsonProperty
import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants.{APP_MAIN_FOLDER, SYSTEM_SEPARATOR}
import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.stringPathToPath
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfterAll, Suites}
import org.scalatestplus.junit.JUnitRunner

private class InnerTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {


  var manager: YamlManager = _
  override def beforeAll(): Unit = {
    this.manager = new YamlManager
  }


  "A YamlManager" when {
    "Save o read file" should {
      "Save a yaml from class" in {
        this.manager
          .saveYaml(Paths.get(APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "Test.yaml"))(TestClass(1)) shouldBe true

      }
      "Read a yaml from file" in {
        this.manager
          .loadYaml(Paths
                      .get(APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "Test.yaml"))(classOf[TestClass]) shouldBe Some(TestClass(1))
      }
    }


  }

}

@RunWith(classOf[JUnitRunner])
class YamlManagerTest extends Suites(new InnerTest) with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    FileManager.deleteLocalFile(APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "Test.yaml")
  }

  override def beforeAll(): Unit = {
    FileManager.createLocalDirectoryTreeFromFile(APP_MAIN_FOLDER)
  }
}
case class TestClass(
  @JsonProperty xx: Int
)
