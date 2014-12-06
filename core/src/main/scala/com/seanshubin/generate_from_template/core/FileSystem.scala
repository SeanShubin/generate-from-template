package com.seanshubin.generate_from_template.core

import java.nio.file.Path

trait FileSystem {
  def allFilesAndDirectories(path: Path): Seq[Path]
}
