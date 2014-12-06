package com.seanshubin.generate_from_template.core

import java.nio.file.Path

class FileSystemImpl(fileSystemIntegration: FileSystemIntegration) extends FileSystem {
  override def allFilesAndDirectories(path: Path): Seq[Path] = {
    val fileVisitor = new FileVisitorThatCollectsAllFilesAndDirectories
    fileSystemIntegration.walkFileTree(path, fileVisitor)
    fileVisitor.filesVisited
  }
}
