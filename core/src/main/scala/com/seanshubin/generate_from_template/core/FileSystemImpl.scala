package com.seanshubin.generate_from_template.core

import java.nio.file.{FileVisitor, Files, Path}

class FileSystemImpl extends FileSystem {
  override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = Files.walkFileTree(start, visitor)

  override def writeBytes(path: Path, bytes: Array[Byte]): Unit = Files.write(path, bytes)

  override def createDirectories(path: Path): Unit = Files.createDirectories(path)
}
