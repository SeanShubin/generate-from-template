package com.seanshubin.generate_from_template.core

import java.nio.file.{FileVisitor, Path}

trait FileSystem {
  def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path])
  def writeBytes(path: Path, bytes: Array[Byte])
  def createDirectories(path: Path)
}
