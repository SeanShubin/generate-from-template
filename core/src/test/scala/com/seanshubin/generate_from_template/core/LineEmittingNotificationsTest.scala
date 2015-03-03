package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import com.seanshubin.devon.core.devon.DefaultDevonMarshaller
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class LineEmittingNotificationsTest extends FunSuite {

  class LineEmitter extends Function[String, Unit] {
    val lines = new ArrayBuffer[String]

    override def apply(s: String): Unit = lines.append(s)
  }

  test("create directories") {
    val lineEmitter = new LineEmitter
    val devonMarshaller = DefaultDevonMarshaller
    val notifications = new LineEmittingNotifications(devonMarshaller, lineEmitter)
    val path = Paths.get("foo", "bar")
    val pathString = path.toString
    notifications.createDirectories(path)
    assert(lineEmitter.lines.size === 1)
    assert(lineEmitter.lines(0) === s"creating directory $pathString")
  }

  test("store string into file") {
    val lineEmitter = new LineEmitter
    val devonMarshaller = DefaultDevonMarshaller
    val notifications = new LineEmittingNotifications(devonMarshaller, lineEmitter)
    val path = Paths.get("foo", "bar")
    val pathString = path.toString
    val stringToStore = "content"
    notifications.storeStringIntoFile(stringToStore, path)
    assert(lineEmitter.lines.size === 1)
    assert(lineEmitter.lines(0) === s"storing 7 characters into $pathString")
  }
}
