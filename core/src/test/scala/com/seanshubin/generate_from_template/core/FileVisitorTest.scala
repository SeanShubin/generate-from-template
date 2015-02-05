package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import org.scalatest.FunSuite

class FileVisitorTest extends FunSuite {
  test("empty") {
    val fileVisitor = new FileVisitorThatCollectsAllFilesAndDirectories(Nil, Nil)
    assert(fileVisitor.filesVisited === Seq())
  }

  test("collect directory") {
    val fileVisitor = new FileVisitorThatCollectsAllFilesAndDirectories(Nil, Nil)
    val dir = Paths.get("dir")
    val attrs = null
    fileVisitor.preVisitDirectory(dir, attrs)
    assert(fileVisitor.filesVisited === Seq(dir))
  }

  test("collect file") {
    val fileVisitor = new FileVisitorThatCollectsAllFilesAndDirectories(Nil, Nil)
    val file = Paths.get("file")
    val attrs = null
    fileVisitor.visitFile(file, attrs)
    assert(fileVisitor.filesVisited === Seq(file))
  }

  test("ignore directory") {
    val ignoreIfNameContainsA = """.*a.*"""
    val fileVisitor = new FileVisitorThatCollectsAllFilesAndDirectories(Seq(ignoreIfNameContainsA), Nil)
    val ignoreMe = Paths.get("foo", "bar")
    val doNotIgnoreMe = Paths.get("bar", "foo")
    val attrs = null
    fileVisitor.preVisitDirectory(ignoreMe, attrs)
    fileVisitor.preVisitDirectory(doNotIgnoreMe, attrs)
    assert(fileVisitor.filesVisited === Seq(doNotIgnoreMe))
  }

  test("ignore file") {
    val ignoreIfNameContainsA = """.*a.*"""
    val fileVisitor = new FileVisitorThatCollectsAllFilesAndDirectories(Nil, Seq(ignoreIfNameContainsA))
    val ignoreMe = Paths.get("foo", "bar")
    val doNotIgnoreMe = Paths.get("bar", "foo")
    val attrs = null
    fileVisitor.visitFile(ignoreMe, attrs)
    fileVisitor.visitFile(doNotIgnoreMe, attrs)
    assert(fileVisitor.filesVisited === Seq(doNotIgnoreMe))
  }
}
