package com.seanshubin.generate_from_template.core

import java.nio.file.Path

case class CopyFileCommand(origin:Path, destination:Path, textReplacements:Map[String, String])
