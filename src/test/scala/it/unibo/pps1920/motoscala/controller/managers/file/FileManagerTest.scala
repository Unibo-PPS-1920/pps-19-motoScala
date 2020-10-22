package it.unibo.pps1920.motoscala.controller.managers.file

import it.unibo.pps1920.motoscala.controller.managers.file.FileConstants.{SystemSeparator, UserHome}
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfterAll, Suites}
import org.scalatestplus.junit.JUnitRunner


private class InnerTestFileManager extends AnyWordSpec with Matchers {

  "A File manager" when {
    "Manage files" should {
      "Create Directory Tree" in {
        FileManager.createLocalDirectoryTreeFromFile(UserHome + SystemSeparator + "Test")
        (UserHome + SystemSeparator + "Test" + SystemSeparator).toFile.isDirectory shouldBe true
      }
      "Create Directory" in {
        FileManager.createLocalDirectory(UserHome + SystemSeparator + "Test" + SystemSeparator + "TestFolder")
        (UserHome + SystemSeparator + "Test" + SystemSeparator + "TestFolder").toFile.isDirectory shouldBe true
      }
      "Create local file" in {
        FileManager.createLocalFile(UserHome + SystemSeparator + "Test" + SystemSeparator + "Test.txt")
        (UserHome + SystemSeparator + "Test" + SystemSeparator + "Test.txt").toFile.exists() shouldBe true

      }
      "Delete local file" in {
        FileManager.deleteLocalFile(UserHome + SystemSeparator + "Test" + SystemSeparator + "Test.txt")
        (UserHome + SystemSeparator + "Test" + SystemSeparator + "Test.txt").toFile.exists() shouldBe false
      }
      "List file names in directory" in {
        FileManager.createLocalFile(UserHome + SystemSeparator + "Test1" + SystemSeparator + "Test.txt")
        FileManager.createLocalFile(UserHome + SystemSeparator + "Test2" + SystemSeparator + "Test.txt")

        FileManager.getListFiles(UserHome + SystemSeparator + "Test")
        (UserHome + SystemSeparator + "Test").toFile.listFiles().length shouldBe 1
      }


    }
  }
}

@RunWith(classOf[JUnitRunner])
class FileManagerTest extends Suites(new InnerTestFileManager) with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    FileManager.deleteLocalFile(UserHome + SystemSeparator + "Test" + SystemSeparator + "TestFolder")
    FileManager.deleteLocalFile(UserHome + SystemSeparator + "Test")
  }
}
