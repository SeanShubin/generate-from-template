package com.seanshubin.generate_from_template.core

import java.nio.file.Path

trait Notifications {
  def effectiveConfiguration(configuration: Configuration)

  def configurationError(lines: Seq[String])

  def topLevelException(exception: Throwable)

  def createDirectories(path: Path): Unit

  def storeStringIntoFile(s: String, path: Path): Unit
}
