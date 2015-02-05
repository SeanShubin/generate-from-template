package com.seanshubin.generate_from_template.core

import java.nio.file.Path

class RunnerImpl(templateDirectory: Path,
                 destinationDirectory: Path,
                 directoryReplacements: Map[Path, Path],
                 fileSystem: FileSystem,
                 commandFactory: CommandFactory,
                 commandExecutor: CommandExecutor,
                 ignoreDirectoryNamePatterns: Seq[String],
                 ignoreFileNamePatterns: Seq[String]) extends Runner {
  override def run(): Unit = {
    val allFiles = fileSystem.allFilesAndDirectories(templateDirectory, ignoreDirectoryNamePatterns, ignoreFileNamePatterns)
    val commands = commandFactory.generateCommands(allFiles, templateDirectory, destinationDirectory, directoryReplacements)
    commands.foreach(commandExecutor.execute)
  }
}
