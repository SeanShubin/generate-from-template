package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import com.seanshubin.utility.json.JsonMarshaller

class LauncherImpl(commandLineArguments: Seq[String], fileSystem: FileSystem, jsonMarshaller: JsonMarshaller, createRunner: Configuration => Runner) extends Launcher {
  override def launch(): Unit = {
    val configFileName = commandLineArguments(0)
    val configFilePath = Paths.get(configFileName)
    val configText = fileSystem.loadFileIntoString(configFilePath)
    val jsonConfiguration = jsonMarshaller.fromJson(configText, classOf[JsonConfiguration])
    val runner = createRunner(jsonConfiguration.toConfiguration)
    runner.run()
  }
}
