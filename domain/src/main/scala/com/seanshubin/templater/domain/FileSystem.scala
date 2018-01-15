package com.seanshubin.templater.domain

import java.nio.file.Path

trait FileSystem {
  def allFilesAndDirectories(path: Path, ignoreDirectoryNamePatterns: Seq[String], ignoreFileNamePatterns: Seq[String]): Seq[Path]

  def loadFileIntoString(path: Path): String

  def isDirectory(path: Path): Boolean

  def storeStringIntoFile(s: String, path: Path)
}
