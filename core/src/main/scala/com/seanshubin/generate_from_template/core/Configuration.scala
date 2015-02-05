package com.seanshubin.generate_from_template.core

import java.nio.file.Path

case class Configuration(templateDirectory: Path,
                         destinationDirectory: Path,
                         directoryReplacements: Map[Path, Path],
                         textReplacements: Map[String, String],
                         ignoreDirectoryNamePatterns: Seq[String],
                         ignoreFileNamePatterns: Seq[String])
