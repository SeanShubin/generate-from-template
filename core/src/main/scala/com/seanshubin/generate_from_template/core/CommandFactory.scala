package com.seanshubin.generate_from_template.core

import java.nio.file.Path

trait CommandFactory {
  def generateCommands(sourcePaths: Seq[Path],
                       templateDirectory: Path,
                       destinationDirectory: Path,
                       directoryReplacements: Map[Path, Path]): Seq[CopyFileCommand]
}
