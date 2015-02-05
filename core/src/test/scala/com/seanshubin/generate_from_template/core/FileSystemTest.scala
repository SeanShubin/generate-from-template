package com.seanshubin.generate_from_template.core

import java.nio.charset.StandardCharsets
import java.nio.file.{FileVisitor, Path, Paths}

import com.seanshubin.utility.file_system.FileSystemIntegration
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class FileSystemTest extends FunSuite with EasyMockSugar {
  val charset = StandardCharsets.UTF_8

  def path(first: String, more: String*): Path = Paths.get(first, more: _*)

  abstract class AbstractFileSystemIntegration extends FileSystemIntegration {
    override def readAllBytes(path: Path): Array[Byte] = ???

    override def createDirectories(path: Path): Path = ???

    override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Path = ???

    override def write(path: Path, bytes: Array[Byte]): Path = ???

    override def isDirectory(path: Path): Boolean = ???

    override def deleteIfExists(path: Path): Boolean = ???

    override def exists(path: Path): Boolean = ???
  }

  class FakeFileSystemIntegration extends AbstractFileSystemIntegration {
    override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Path = {
      visitor.preVisitDirectory(Paths.get("target", "walk-file-tree-test"), null)
      visitor.preVisitDirectory(Paths.get("target", "walk-file-tree-test", "aaa"), null)
      visitor.preVisitDirectory(Paths.get("target", "walk-file-tree-test", "aaa", "ccc"), null)
      visitor.visitFile(Paths.get("target", "walk-file-tree-test", "aaa", "ccc", "ddd.txt"), null)
      visitor.postVisitDirectory(Paths.get("target", "walk-file-tree-test", "aaa", "ccc"), null)
      visitor.preVisitDirectory(Paths.get("target", "walk-file-tree-test", "aaa", "bbb"), null)
      visitor.visitFile(Paths.get("target", "walk-file-tree-test", "aaa", "bbb", "ccc.txt"), null)
      visitor.postVisitDirectory(Paths.get("target", "walk-file-tree-test", "aaa", "bbb"), null)
      visitor.visitFile(Paths.get("target", "walk-file-tree-test", "aaa", "bbb.txt"), null)
      visitor.postVisitDirectory(Paths.get("target", "walk-file-tree-test", "aaa"), null)
      visitor.visitFile(Paths.get("target", "walk-file-tree-test", "aaa.txt"), null)
      visitor.postVisitDirectory(Paths.get("target", "walk-file-tree-test"), null)
      start
    }
  }

  test("can load files and directories") {
    val fileSystemIntegration: FileSystemIntegration = new FakeFileSystemIntegration
    val notifications = mock[Notifications]
    val fileSystem: FileSystem = new FileSystemImpl(fileSystemIntegration, charset, notifications)
    val ignoreDirectoryNames = Seq()
    val ignoreFileNamePatterns = Seq()
    val actual = fileSystem.allFilesAndDirectories(Paths.get("foo"), ignoreDirectoryNames, ignoreFileNamePatterns)
    val expected = Seq(
      path("target", "walk-file-tree-test"),
      path("target", "walk-file-tree-test", "aaa"),
      path("target", "walk-file-tree-test", "aaa", "ccc"),
      path("target", "walk-file-tree-test", "aaa", "ccc", "ddd.txt"),
      path("target", "walk-file-tree-test", "aaa", "bbb"),
      path("target", "walk-file-tree-test", "aaa", "bbb", "ccc.txt"),
      path("target", "walk-file-tree-test", "aaa", "bbb.txt"),
      path("target", "walk-file-tree-test", "aaa.txt")
    )
    val compareResult = SequenceComparison.compare(actual, expected)
    assert(compareResult.areSame, compareResult.toMultipleLineString.mkString("\n", "\n", ""))
  }

  test("load file into string") {
    val fileSystemIntegration = mock[FileSystemIntegration]
    val notifications = null
    val fileSystem = new FileSystemImpl(fileSystemIntegration, charset, notifications)
    val path = Paths.get("hello.txt")
    val expected = "Hello, world!"
    expecting {
      fileSystemIntegration.readAllBytes(path).andReturn(expected.getBytes(charset))
    }
    whenExecuting(fileSystemIntegration) {
      val actual = fileSystem.loadFileIntoString(path)
      assert(actual === expected)
    }
  }

  test("store string into file") {
    val path = Paths.get("myDir", "hello.txt")
    val content = "Hello, world!"
    val bytes = content.getBytes(charset)
    val fileSystemIntegration = new AbstractFileSystemIntegration {
      override def createDirectories(thePath: Path): Path = {
        assert(thePath === path.getParent)
        thePath
      }

      override def write(thePath: Path, theBytes: Array[Byte]): Path = {
        assert(thePath === path)
        assert(theBytes === bytes)
        thePath
      }
    }
    val notifications = mock[Notifications]
    val fileSystem = new FileSystemImpl(fileSystemIntegration, charset, notifications)
    expecting {
      notifications.storeStringIntoFile(content, path)
    }
    whenExecuting(notifications) {
      fileSystem.storeStringIntoFile(content, path)
    }
  }
}
