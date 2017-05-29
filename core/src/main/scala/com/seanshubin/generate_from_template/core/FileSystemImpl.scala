package com.seanshubin.generate_from_template.core

import java.nio.charset.Charset
import java.nio.file.Path

class FileSystemImpl(files: FilesContract,
                     charset: Charset,
                     notifications: Notifications) extends FileSystem {
  override def allFilesAndDirectories(path: Path, ignoreDirectoryNamePatterns: Seq[String], ignoreFileNamePatterns: Seq[String]): Seq[Path] = {
    val fileVisitor = new FileVisitorThatCollectsAllFilesAndDirectories(ignoreDirectoryNamePatterns, ignoreFileNamePatterns)
    files.walkFileTree(path, fileVisitor)
    fileVisitor.filesVisited
  }

  override def loadFileIntoString(path: Path): String = {
    val bytes = files.readAllBytes(path)
    val string = new String(bytes, charset)
    string
  }

  override def isDirectory(path: Path): Boolean = {
    files.isDirectory(path)
  }

  override def storeStringIntoFile(s: String, path: Path): Unit = {
    notifications.storeStringIntoFile(s, path)
    files.createDirectories(path.getParent)
    val bytes = s.getBytes(charset)
    files.write(path, bytes)
  }
}
