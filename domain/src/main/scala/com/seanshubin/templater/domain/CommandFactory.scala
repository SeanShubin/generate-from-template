package com.seanshubin.templater.domain

import java.nio.file.Path

trait CommandFactory {
  def generateCommands(sourcePaths: Seq[Path],
                       templateDirectory: Path,
                       destinationDirectory: Path,
                       directoryReplacements: Map[Path, Path]): Seq[CopyFileCommand]
}
