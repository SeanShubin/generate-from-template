package com.seanshubin.templater.domain

import java.nio.file.Path

case class CopyFileCommand(origin: Path, destination: Path)
