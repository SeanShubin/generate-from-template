package com.seanshubin.generate_from_template.core

import java.nio.charset.StandardCharsets
import java.nio.file.{FileVisitor, Path, Paths}

import com.seanshubin.utility.filesystem.{FileSystemIntegration, FileSystemIntegrationNotImplemented}
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class FileSystemTest extends FunSuite {
  val charset = StandardCharsets.UTF_8

  def path(first: String, more: String*): Path = Paths.get(first, more: _*)

  test("can load files and directories") {
    val fileSystemIntegration: FileSystemIntegration = new FakeFileSystemIntegration(null, null)
    val sideEffects: ArrayBuffer[(String, Any)] = new ArrayBuffer()
    val notifications = new FakeNotifications(sideEffects)
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
    assert(sideEffects === Seq())
  }

  test("load file into string") {
    val path = Paths.get("hello.txt")
    val expected = "Hello, world!"
    val fileSystemIntegration = new FakeFileSystemIntegration(path, expected)
    val notifications = null
    val fileSystem = new FileSystemImpl(fileSystemIntegration, charset, notifications)
    val actual = fileSystem.loadFileIntoString(path)
    assert(actual === expected)
  }

  test("store string into file") {
    val path = Paths.get("myDir", "hello.txt")
    val content = "Hello, world!"
    val bytes = content.getBytes(charset)
    val fileSystemIntegration = new FileSystemIntegrationNotImplemented {
      override def createDirectories(thePath: Path): Path = {
        assert(thePath === path.getParent)
        thePath
      }

      override def write(thePath: Path, theBytes: Seq[Byte]): Path = {
        assert(thePath === path)
        assert(theBytes === bytes)
        thePath
      }
    }
    val sideEffects: ArrayBuffer[(String, Any)] = new ArrayBuffer()
    val notifications = new FakeNotifications(sideEffects)
    val fileSystem = new FileSystemImpl(fileSystemIntegration, charset, notifications)
    fileSystem.storeStringIntoFile(content, path)
    assert(sideEffects === Seq(("notifications.storeStringIntoFile", ("Hello, world!", path))))
  }

  class FakeFileSystemIntegration(pathToRead: Path, stringToReturn: String) extends FileSystemIntegrationNotImplemented {
    override def readAllBytes(path: Path): Seq[Byte] = {
      assert(path === pathToRead)
      stringToReturn.getBytes(charset)
    }

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

}
