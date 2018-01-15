package com.seanshubin.templater.domain

import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite

class CommandExecutorTest extends FunSuite {
  test("ignore copy command if origin is a directory") {
    val isDirectoryResult = true
    val fileSystem = new FakeFileSystem(isDirectoryResult, null, null, null, null)
    val textReplacements = Map("aaa" -> "bbb", "ccc" -> "ddd")
    val commandExecutor = new CommandExecutorImpl(fileSystem, textReplacements)
    val origin = Paths.get("origin")
    val destination = Paths.get("destination")
    val command = CopyFileCommand(origin, destination)
    commandExecutor.execute(command)
  }
  test("execute copy command") {
    val isDirectoryResult = false
    val contents = "aaa bbb ccc ddd eee fff aaa"
    val expectedReplaced = "bbb bbb ddd ddd eee fff bbb"
    val destination = Paths.get("destination")
    val origin = Paths.get("origin")
    val fileSystem = new FakeFileSystem(isDirectoryResult, origin, contents, expectedReplaced, destination)
    val textReplacements = Map("aaa" -> "bbb", "ccc" -> "ddd")
    val commandExecutor = new CommandExecutorImpl(fileSystem, textReplacements)
    val command = CopyFileCommand(origin, destination)
    commandExecutor.execute(command)
  }

  class FakeFileSystem(isDirectoryResult: Boolean, expectedOrigin: Path, loadResult: String, expectToStore: String, pathToStore: Path) extends FileSystem {
    override def allFilesAndDirectories(path: Path, ignoreDirectoryNamePatterns: Seq[String], ignoreFileNamePatterns: Seq[String]): Seq[Path] = ???

    override def storeStringIntoFile(s: String, path: Path): Unit = {
      assert(s === expectToStore)
      assert(path === pathToStore)
    }

    override def loadFileIntoString(path: Path): String = {
      assert(path === expectedOrigin)
      loadResult
    }

    override def isDirectory(path: Path): Boolean = isDirectoryResult
  }

}
