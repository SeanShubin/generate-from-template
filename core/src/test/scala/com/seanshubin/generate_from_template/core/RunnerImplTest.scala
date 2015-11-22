package com.seanshubin.generate_from_template.core

import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class RunnerImplTest extends FunSuite {
  test("application flow") {
    val templateDirectory = Paths.get("foo", "bar")
    val templateFilesAndDirectories = Seq(
      templateDirectory,
      templateDirectory.resolve("baz"),
      templateDirectory.resolve("baz").resolve("content.txt")
    )
    val ignoreDirectoryNamePatterns = Seq("lll")
    val ignoreFileNamePatterns = Seq("mmm")
    val fileSystem = new FakeFileSystem(templateFilesAndDirectories, templateDirectory, ignoreDirectoryNamePatterns, ignoreFileNamePatterns)
    val destinationDirectory = Paths.get("aaa", "bbb", "ccc")
    val directoryReplacements = Map(Paths.get("ddd", "eee") -> Paths.get("fff", "ggg"))
    val sideEffects: ArrayBuffer[(String, Any)] = new ArrayBuffer()
    val commandExecutor = new FakeCommandExecutor(sideEffects)
    val command1 = CopyFileCommand(Paths.get("copy-from-1"), Paths.get("copy-to-1"))
    val command2 = CopyFileCommand(Paths.get("copy-from-2"), Paths.get("copy-to-2"))
    val commands = Seq(
      command1,
      command2
    )
    val commandFactory = new FakeCommandFactory(commands, templateFilesAndDirectories, templateDirectory, destinationDirectory, directoryReplacements)
    val runner: Runnable = new RunnerImpl(
      templateDirectory,
      destinationDirectory,
      directoryReplacements,
      fileSystem,
      commandFactory,
      commandExecutor,
      ignoreDirectoryNamePatterns,
      ignoreFileNamePatterns)
    runner.run()

    assert(sideEffects === Seq(("commandExecutor.execute", command1), ("commandExecutor.execute", command2)))
  }

  class FakeFileSystem(allFilesAndDirectoriesResult: Seq[Path],
                       expectedPath: Path,
                       expectedIgnoreDirectoryNamePatterns: Seq[String],
                       expectedIgnoreFileNamePatterns: Seq[String]) extends FileSystem {
    override def allFilesAndDirectories(path: Path, ignoreDirectoryNamePatterns: Seq[String], ignoreFileNamePatterns: Seq[String]): Seq[Path] = {
      assert(path === expectedPath)
      assert(ignoreDirectoryNamePatterns === expectedIgnoreDirectoryNamePatterns)
      assert(ignoreFileNamePatterns === expectedIgnoreFileNamePatterns)
      allFilesAndDirectoriesResult
    }

    override def storeStringIntoFile(s: String, path: Path): Unit = ???

    override def loadFileIntoString(path: Path): String = ???

    override def isDirectory(path: Path): Boolean = ???
  }

  class FakeCommandFactory(generateCommandsResult: Seq[CopyFileCommand],
                           expectedSourcePaths: Seq[Path],
                           expectedTemplateDirectory: Path,
                           expectedDestinationDirectory: Path,
                           expectedDirectoryReplacements: Map[Path, Path]) extends CommandFactory {
    override def generateCommands(sourcePaths: Seq[Path],
                                  templateDirectory: Path,
                                  destinationDirectory: Path,
                                  directoryReplacements: Map[Path, Path]): Seq[CopyFileCommand] = {
      assert(sourcePaths === expectedSourcePaths)
      assert(templateDirectory === expectedTemplateDirectory)
      assert(destinationDirectory === expectedDestinationDirectory)
      assert(directoryReplacements === expectedDirectoryReplacements)
      generateCommandsResult
    }
  }

  class FakeCommandExecutor(sideEffects: ArrayBuffer[(String, Any)]) extends CommandExecutor {
    def append(name: String, value: Any): Unit = {
      sideEffects.append(("commandExecutor." + name, value))
    }

    override def execute(command: CopyFileCommand): Unit = append("execute", command)
  }

}
