package com.seanshubin.generate_from_template.core

import java.nio.file.Path

trait Notifications {
  def createDirectories(path: Path): Unit

  def storeStringIntoFile(s: String, path: Path): Unit
}
