package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

case class JsonConfiguration(templateDirectory: String,
                             destinationDirectory: String,
                             directoryReplacements: Map[String, String],
                             textReplacements: Map[String, String],
                             ignoreDirectoryNamePatterns: Seq[String],
                             ignoreFileNamePatterns: Seq[String]) {
  def toConfiguration: Configuration = {
    val newTemplateDirectory = Paths.get(templateDirectory)
    val newDestinationDirectory = Paths.get(destinationDirectory)
    def stringEntryToPathEntry(stringEntry: (String, String)) = {
      val (key, value) = stringEntry
      (Paths.get(key), Paths.get(value))
    }
    val newDirectoryReplacements = directoryReplacements.map(stringEntryToPathEntry)
    Configuration(
      newTemplateDirectory,
      newDestinationDirectory,
      newDirectoryReplacements,
      textReplacements,
      ignoreDirectoryNamePatterns,
      ignoreFileNamePatterns)
  }
}
