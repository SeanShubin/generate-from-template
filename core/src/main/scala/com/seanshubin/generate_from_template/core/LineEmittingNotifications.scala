package com.seanshubin.generate_from_template.core

import java.io.{PrintWriter, StringWriter}
import java.nio.file.Path

import com.seanshubin.devon.domain.DevonMarshaller

class LineEmittingNotifications(devonMarshaller: DevonMarshaller, emit: String => Unit) extends Notifications {
  override def topLevelException(exception: Throwable): Unit = {
    exceptionLines(exception).foreach(emit)
  }

  override def effectiveConfiguration(configuration: Configuration): Unit = {
    val devon = devonMarshaller.fromValue(configuration)
    val pretty = devonMarshaller.toPretty(devon)
    emit("Effective configuration:")
    pretty.foreach(emit)
  }

  override def configurationError(lines: Seq[String]): Unit = {
    lines.foreach(emit)
  }

  override def createDirectories(path: Path): Unit = {
    emit(s"creating directory $path")
  }

  override def storeStringIntoFile(s: String, path: Path): Unit = {
    val charCount = s.size
    emit(s"storing $charCount characters into $path")
  }

  private def exceptionLines(ex: Throwable): Seq[String] = {
    val stringWriter = new StringWriter()
    val printWriter = new PrintWriter(stringWriter)
    ex.printStackTrace(printWriter)
    val s = stringWriter.toString
    val lines = s.split( """\r\n|\r|\n""").toSeq
    lines
  }
}
