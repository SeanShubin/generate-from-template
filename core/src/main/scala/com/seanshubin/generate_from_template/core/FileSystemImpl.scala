package com.seanshubin.generate_from_template.core

import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.utility.filesystem.FileSystemIntegration

class FileSystemImpl(fileSystemIntegration: FileSystemIntegration,
                     charset: Charset,
                     notifications: Notifications) extends FileSystem {
  override def allFilesAndDirectories(path: Path, ignoreDirectoryNamePatterns: Seq[String], ignoreFileNamePatterns: Seq[String]): Seq[Path] = {
    val fileVisitor = new FileVisitorThatCollectsAllFilesAndDirectories(ignoreDirectoryNamePatterns, ignoreFileNamePatterns)
    fileSystemIntegration.walkFileTree(path, fileVisitor)
    fileVisitor.filesVisited
  }

  override def loadFileIntoString(path: Path): String = {
    val bytes = fileSystemIntegration.readAllBytes(path)
    val string = new String(bytes.toArray, charset)
    string
  }

  override def isDirectory(path: Path): Boolean = {
    fileSystemIntegration.isDirectory(path)
  }

  override def storeStringIntoFile(s: String, path: Path): Unit = {
    notifications.storeStringIntoFile(s, path)
    fileSystemIntegration.createDirectories(path.getParent)
    val bytes = s.getBytes(charset)
    fileSystemIntegration.write(path, bytes)
  }
}
