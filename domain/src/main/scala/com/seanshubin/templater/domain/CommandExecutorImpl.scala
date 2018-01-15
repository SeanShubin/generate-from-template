package com.seanshubin.templater.domain

class CommandExecutorImpl(fileSystem: FileSystem, textReplacements: Map[String, String]) extends CommandExecutor {
  override def execute(command: CopyFileCommand): Unit = {
    if (!fileSystem.isDirectory(command.origin)) {
      val contents = fileSystem.loadFileIntoString(command.origin)
      val updatedContents = textReplacements.foldLeft(contents)(replaceText)
      fileSystem.storeStringIntoFile(updatedContents, command.destination)
    }
  }

  private def replaceText(soFar: String, replacement: (String, String)): String = {
    val (before, after) = replacement
    soFar.replaceAllLiterally(before, after)
  }
}
