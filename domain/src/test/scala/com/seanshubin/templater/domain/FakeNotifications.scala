package com.seanshubin.templater.domain

import java.nio.file.Path

import scala.collection.mutable.ArrayBuffer

class FakeNotifications(sideEffects: ArrayBuffer[(String, Any)]) extends Notifications {
  def append(name: String, value: Any): Unit = {
    sideEffects.append(("notifications." + name, value))
  }

  override def configurationError(lines: Seq[String]): Unit = append("configurationError", lines)

  override def effectiveConfiguration(configuration: Configuration): Unit = append("effectiveConfiguration", configuration)

  override def topLevelException(exception: Throwable): Unit = append("topLevelException", exception)

  override def createDirectories(path: Path): Unit = append("createDirectories", path)

  override def storeStringIntoFile(s: String, path: Path): Unit = append("storeStringIntoFile", (s, path))
}
