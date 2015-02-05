package com.seanshubin.generate_from_template.core

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class LauncherTest extends FunSuite with EasyMockSugar {
  test("launch") {
    val commandLineArguments = Seq("some-configuration-file")
    val createRunner = mock[Configuration => Runner]
    val jsonConfiguration = JsonConfiguration(
      templateDirectory = "template directory",
      destinationDirectory = "destination directory",
      directoryReplacements = Map("aaa" -> "bbb"),
      textReplacements = Map("ccc" -> "ddd"),
      ignoreDirectoryNamePatterns = Seq("eee", "fff"),
      ignoreFileNamePatterns = Seq("ggg", "hhh")
    )
    val configuration = jsonConfiguration.toConfiguration
    val runner = mock[Runner]
    val fileSystem = mock[FileSystem]
    val jsonMarshaller = mock[JsonMarshaller]
    val launcher = new LauncherImpl(commandLineArguments, fileSystem, jsonMarshaller, createRunner)

    expecting {
      createRunner(configuration).andReturn(runner)
      runner.run()
    }

    whenExecuting(createRunner, runner) {
      launcher.launch()
    }
  }
}
