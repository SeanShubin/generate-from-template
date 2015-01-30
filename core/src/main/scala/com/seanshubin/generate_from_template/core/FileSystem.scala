package com.seanshubin.generate_from_template.core

import java.nio.file.Path

trait FileSystem {
  def allFilesAndDirectories(path: Path, ignoreDirectoryNames: Seq[Path], ignoreFileNamePatterns: Seq[String]): Seq[Path]

  def loadFileIntoString(path: Path): String

  def isDirectory(path: Path): Boolean

  def storeStringIntoFile(s: String, path: Path)
}
