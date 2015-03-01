package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class RunnerImplTest extends FunSuite with EasyMockSugar {
  test("application flow") {
    val fileSystem = mock[FileSystem]
    val templateDirectory = Paths.get("foo", "bar")
    val destinationDirectory = Paths.get("aaa", "bbb", "ccc")
    val directoryReplacements = Map(Paths.get("ddd", "eee") -> Paths.get("fff", "ggg"))
    val textReplacements = Map("hhh" -> "iii", "jjj" -> "kkk")
    val commandFactory = mock[CommandFactory]
    val commandExecutor = mock[CommandExecutor]
    val ignoreDirectoryNamePatterns = Seq("lll")
    val ignoreFileNamePatterns = Seq("mmm")
    val runner: Runner = new RunnerImpl(
      templateDirectory,
      destinationDirectory,
      directoryReplacements,
      fileSystem,
      commandFactory,
      commandExecutor,
      ignoreDirectoryNamePatterns,
      ignoreFileNamePatterns)
    val templateFilesAndDirectories = Seq(
      templateDirectory,
      templateDirectory.resolve("baz"),
      templateDirectory.resolve("baz").resolve("content.txt")
    )
    val command1 = CopyFileCommand(Paths.get("copy-from-1"), Paths.get("copy-to-1"))
    val command2 = CopyFileCommand(Paths.get("copy-from-2"), Paths.get("copy-to-2"))
    val commands = Seq(
      command1,
      command2
    )
    expecting {
      fileSystem.allFilesAndDirectories(templateDirectory, ignoreDirectoryNamePatterns, ignoreFileNamePatterns).andReturn(templateFilesAndDirectories)
      commandFactory.generateCommands(templateFilesAndDirectories, templateDirectory, destinationDirectory, directoryReplacements).andReturn(commands)
      commandExecutor.execute(command1)
      commandExecutor.execute(command2)
    }
    whenExecuting(fileSystem, commandFactory, commandExecutor) {
      runner.run()
    }
  }
}
