package com.seanshubin.generate_from_template.core

import java.nio.file.Path

class NotificationsImpl(emitLine: String => Unit) extends Notifications {
  override def createDirectories(path: Path): Unit = {
    emitLine(s"creating directory $path")
  }

  override def storeStringIntoFile(s: String, path: Path): Unit = {
    val charCount = s.size
    emitLine(s"storing $charCount characters into $path")
  }
}
