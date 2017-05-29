package com.seanshubin.generate_from_template.core

import java.nio.file.{Path, Paths}

class CommandFactoryImpl extends CommandFactory {
  override def generateCommands(sourcePaths: Seq[Path],
                                templateDirectory: Path,
                                destinationDirectory: Path,
                                directoryReplacements: Map[Path, Path]): Seq[CopyFileCommand] = {
    def applyReplacements(path: Path) = {
      def applyReplacement(soFar: String, replacement: (Path, Path)) = {
        val (before, after) = replacement
        val updated = soFar.replaceAllLiterally(before.toString, after.toString)
        updated
      }

      Paths.get(directoryReplacements.foldLeft(path.toString)(applyReplacement))
    }

    def generateCommand(sourcePath: Path) = {
      val relativePath = templateDirectory.relativize(sourcePath)
      val destinationPath = destinationDirectory.resolve(relativePath)
      val replacedPath = applyReplacements(destinationPath)
      CopyFileCommand(
        origin = sourcePath,
        destination = replacedPath
      )
    }

    sourcePaths.map(generateCommand)
  }
}
