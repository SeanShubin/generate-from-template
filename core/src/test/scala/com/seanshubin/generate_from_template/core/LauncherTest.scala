package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import com.seanshubin.utility.json.JsonMarshaller
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class LauncherTest extends FunSuite with EasyMockSugar {
  test("launch") {
    val configFileName = "some-configuration-file"
    val configFilePath = Paths.get(configFileName)
    val commandLineArguments = Seq(configFileName)
    val createRunner = mock[Configuration => Runner]
    val jsonConfiguration = JsonConfiguration(
      templateDirectory = "template directory",
      destinationDirectory = "destination directory",
      directoryReplacements = Map("aaa" -> "bbb"),
      textReplacements = Map("ccc" -> "ddd"),
      ignoreDirectoryNamePatterns = Seq("eee", "fff"),
      ignoreFileNamePatterns = Seq("ggg", "hhh")
    )
    val configText = "config-text"
    val configuration = jsonConfiguration.toConfiguration
    val runner = mock[Runner]
    val fileSystem = mock[FileSystem]
    val jsonMarshaller = mock[JsonMarshaller]
    val launcher = new LauncherImpl(commandLineArguments, fileSystem, jsonMarshaller, createRunner)

    expecting {
      fileSystem.loadFileIntoString(configFilePath).andReturn(configText)
      jsonMarshaller.fromJson("config-text", classOf[JsonConfiguration]).andReturn(jsonConfiguration)
      createRunner.apply(configuration).andReturn(runner)
      runner.run()
    }

    whenExecuting(createRunner, runner, jsonMarshaller, fileSystem) {
      launcher.launch()
    }
  }
}
