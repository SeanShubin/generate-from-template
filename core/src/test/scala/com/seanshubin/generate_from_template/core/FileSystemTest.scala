package com.seanshubin.generate_from_template.core

import java.nio.charset.StandardCharsets
import java.nio.file.{FileVisitor, Path, Paths}

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class FileSystemTest extends FunSuite with EasyMockSugar {
  val charset = StandardCharsets.UTF_8

  def path(first: String, more: String*): Path = Paths.get(first, more: _*)

  class FakeFileSystemIntegration extends FileSystemIntegration {
    override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
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
    }

    override def createDirectories(path: Path): Unit = ???

    override def writeBytes(path: Path, bytes: Array[Byte]): Unit = ???
  }

  test("can load files and directories") {
    val fileSystemIntegration: FileSystemIntegration = new FakeFileSystemIntegration
    val fileSystem: FileSystem = new FileSystemImpl(fileSystemIntegration)
    val actual = fileSystem.allFilesAndDirectories(Paths.get("foo"))
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
}
