package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

import scala.collection.mutable.ArrayBuffer

class RunnerTest extends FunSuite with EasyMockSugar {
  test("application flow") {
    val fileSystem = mock[FileSystem]
    val templateDirectory = Paths.get("foo", "bar")
    val destinationDirectory = Paths.get("aaa", "bbb", "ccc")
    val directoryReplacements = Map(Paths.get("ddd", "eee") -> Paths.get("fff", "ggg"))
    val textReplacements = Map("hhh" -> "iii", "jjj" -> "kkk")
    val commandFactory = mock[CommandFactory]
    val lines = new ArrayBuffer[String]()
    val commandExecutor: CommandExecutor = new CommandExecutorImpl(fileSystem, textReplacements)
    val ignoreDirectoryNames = Seq()
    val ignoreFileNamePatterns = Seq()
    val runner: Runner = new RunnerImpl(
      templateDirectory,
      destinationDirectory,
      directoryReplacements,
      fileSystem,
      commandFactory,
      commandExecutor,
      ignoreDirectoryNames,
      ignoreFileNamePatterns)
    runner.run()
    assert(lines.size === 1)
    assert(lines(0) === "Hello, world!")
  }
}
