package it.unibo.pps1920.motoscala.controller.managers.file

import java.nio.file.Paths

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants.{APP_MAIN_FOLDER, SYSTEM_SEPARATOR}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
abstract class YamlManagerTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {


  var manager: YamlManager
  override def beforeAll(): Unit = {
    this.manager = new YamlManager
  }
  case class TestClass(val xx: Int)


  "A YamlManager" when {
    "Save o read file" should {
      "Save a yaml from file" in {
        this.manager.saveYaml(Paths.get(APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "Test.yaml"))(TestClass(1)) shouldBe true
      }
      "Read a yaml from file" in {
        this.manager
          .loadYaml(Paths
                      .get(APP_MAIN_FOLDER + SYSTEM_SEPARATOR + "Test.yaml"))(classOf[DataLevel]) shouldBe Some(TestClass(1))
      }
    }
  }


}
