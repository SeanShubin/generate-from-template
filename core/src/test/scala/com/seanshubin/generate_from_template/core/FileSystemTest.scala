package com.seanshubin.generate_from_template.core

import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, FileVisitor, Path, Paths}

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

import scala.collection.mutable.ArrayBuffer

class FileSystemTest extends FunSuite with EasyMockSugar {
  val charset = StandardCharsets.UTF_8

  class FakeFileVisitor extends FileVisitor[Path] {
    private val filesVisitedBuffer = new ArrayBuffer[Path]
    def filesVisited = filesVisitedBuffer
    override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
      filesVisitedBuffer.append(dir)
      FileVisitResult.CONTINUE
    }

    override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = ???

    override def visitFile(file:Path, attrs: BasicFileAttributes): FileVisitResult = {
      filesVisitedBuffer.append(file)
      FileVisitResult.CONTINUE
    }

    override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE
  }

  test("can walk file tree") {
    val fileSystem:FileSystem = new FileSystemImpl()
    fileSystem.createDirectories(path("target", "walk-file-tree-test", "aaa", "bbb"))
    fileSystem.createDirectories(path("target", "walk-file-tree-test", "aaa", "ccc"))
    writeString(fileSystem,      path("target", "walk-file-tree-test", "aaa.txt"), "hello aaa!")
    writeString(fileSystem,      path("target", "walk-file-tree-test", "aaa", "bbb.txt"), "hello bbb!")
    writeString(fileSystem,      path("target", "walk-file-tree-test", "aaa", "bbb", "ccc.txt"), "hello ccc!")
    writeString(fileSystem,      path("target", "walk-file-tree-test", "aaa", "ccc", "ddd.txt"), "hello ddd!")
    val fileVisitor = new FakeFileVisitor()
    fileSystem.walkFileTree(path("target", "walk-file-tree-test"), fileVisitor)
    val actual = fileVisitor.filesVisited
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

  def path(first:String, more:String*):Path = Paths.get(first, more:_*)

  def writeString(fileSystem:FileSystem, path:Path, text:String): Unit = fileSystem.writeBytes(path, text.getBytes(charset))
}
