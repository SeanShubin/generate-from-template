package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class CommandExecutorTest extends FunSuite with EasyMockSugar {
  test("ignore copy command if origin is a directory") {
    val fileSystem = mock[FileSystem]
    val textReplacements = Map("aaa" -> "bbb", "ccc" -> "ddd")
    val commandExecutor = new CommandExecutorImpl(fileSystem, textReplacements)
    val origin = Paths.get("origin")
    val destination = Paths.get("destination")
    val command = CopyFileCommand(origin, destination)
    expecting {
      fileSystem.isDirectory(origin).andReturn(true)
    }
    whenExecuting(fileSystem) {
      commandExecutor.execute(command)
    }
  }
  test("execute copy command") {
    val fileSystem = mock[FileSystem]
    val textReplacements = Map("aaa" -> "bbb", "ccc" -> "ddd")
    val commandExecutor = new CommandExecutorImpl(fileSystem, textReplacements)
    val origin = Paths.get("origin")
    val destination = Paths.get("destination")
    val command = CopyFileCommand(origin, destination)
    val contents = "aaa bbb ccc ddd eee fff aaa"
    val expectedReplaced = "bbb bbb ddd ddd eee fff bbb"
    expecting {
      fileSystem.isDirectory(origin).andReturn(false)
      fileSystem.loadFileIntoString(origin).andReturn(contents)
      fileSystem.storeStringIntoFile(expectedReplaced, destination)
    }
    whenExecuting(fileSystem) {
      commandExecutor.execute(command)
    }
  }
}
