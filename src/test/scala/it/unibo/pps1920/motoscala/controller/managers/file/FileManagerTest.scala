package it.unibo.pps1920.motoscala.controller.managers.file

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants.{SYSTEM_SEPARATOR, USER_HOME}
import it.unibo.pps1920.motoscala.controller.managers.file.FileManager.stringPathToPath
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfterAll, Suites}
import org.scalatestplus.junit.JUnitRunner


private class InnerTestFileManager extends AnyWordSpec with Matchers {

  "A File manager" when {
    "Manage files" should {
      "Create Directory Tree" in {
        FileManager.createLocalDirectoryTree(USER_HOME + SYSTEM_SEPARATOR + "Test" + SYSTEM_SEPARATOR + "Test.txt")
        (USER_HOME + SYSTEM_SEPARATOR + "Test" + SYSTEM_SEPARATOR).toFile.isDirectory shouldBe true
      }
      "Create Directory" in {
        FileManager.createLocalDirectory(USER_HOME + SYSTEM_SEPARATOR + "Test" + SYSTEM_SEPARATOR + "TestFolder")
        (USER_HOME + SYSTEM_SEPARATOR + "Test" + SYSTEM_SEPARATOR + "TestFolder").toFile.isDirectory shouldBe true
      }
      "Create local file" in {
        FileManager.createLocalFile(USER_HOME + SYSTEM_SEPARATOR + "Test" + SYSTEM_SEPARATOR + "Test.txt")
        (USER_HOME + SYSTEM_SEPARATOR + "Test" + SYSTEM_SEPARATOR + "Test.txt").toFile.exists() shouldBe true

      }
      "Delete local file" in {
        FileManager.deleteLocalFile(USER_HOME + SYSTEM_SEPARATOR + "Test" + SYSTEM_SEPARATOR + "Test.txt")
        (USER_HOME + SYSTEM_SEPARATOR + "Test" + SYSTEM_SEPARATOR + "Test.txt").toFile.exists() shouldBe false
      }
    }
  }
}

@RunWith(classOf[JUnitRunner])
class FileManagerTest extends Suites(new InnerTestFileManager) with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    FileManager.deleteLocalFile(USER_HOME + SYSTEM_SEPARATOR + "Test" + SYSTEM_SEPARATOR + "TestFolder")
    FileManager.deleteLocalFile(USER_HOME + SYSTEM_SEPARATOR + "Test")
  }
}
