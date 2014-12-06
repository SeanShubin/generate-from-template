package com.seanshubin.generate_from_template.core

import java.nio.charset.StandardCharsets
import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite

class FileSystemIntegrationTest extends FunSuite {
  val charset = StandardCharsets.UTF_8

  def path(first: String, more: String*): Path = Paths.get(first, more: _*)

  test("can walk file tree") {
    val fileSystem: FileSystemIntegration = new FileSystemIntegrationImpl()
    fileSystem.createDirectories(path("target", "walk-file-tree-test", "aaa", "bbb"))
    fileSystem.createDirectories(path("target", "walk-file-tree-test", "aaa", "ccc"))
    writeString(fileSystem, path("target", "walk-file-tree-test", "aaa.txt"), "hello aaa!")
    writeString(fileSystem, path("target", "walk-file-tree-test", "aaa", "bbb.txt"), "hello bbb!")
    writeString(fileSystem, path("target", "walk-file-tree-test", "aaa", "bbb", "ccc.txt"), "hello ccc!")
    writeString(fileSystem, path("target", "walk-file-tree-test", "aaa", "ccc", "ddd.txt"), "hello ddd!")
    val fileVisitor = new FileVisitorThatCollectsAllFilesAndDirectories
    fileSystem.walkFileTree(path("target", "walk-file-tree-test"), fileVisitor)
    val actual = fileVisitor.filesVisited.toSet
    val expected = Set(
      path("target", "walk-file-tree-test"),
      path("target", "walk-file-tree-test", "aaa"),
      path("target", "walk-file-tree-test", "aaa", "ccc"),
      path("target", "walk-file-tree-test", "aaa", "ccc", "ddd.txt"),
      path("target", "walk-file-tree-test", "aaa", "bbb"),
      path("target", "walk-file-tree-test", "aaa", "bbb", "ccc.txt"),
      path("target", "walk-file-tree-test", "aaa", "bbb.txt"),
      path("target", "walk-file-tree-test", "aaa.txt")
    )
    val compareResult = SetComparison.compare(actual, expected)
    assert(compareResult.areSame, compareResult.toMultipleLineString.mkString("\n", "\n", ""))
  }

  def writeString(fileSystem: FileSystemIntegration, path: Path, text: String): Unit = fileSystem.writeBytes(path, text.getBytes(charset))
}
