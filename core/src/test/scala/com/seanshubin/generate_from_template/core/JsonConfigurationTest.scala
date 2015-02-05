package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import org.scalatest.FunSuite

class JsonConfigurationTest extends FunSuite {
  test("convert to Configuration") {
    val jsonConfiguration = JsonConfiguration(
      templateDirectory = "template directory",
      destinationDirectory = "destination directory",
      directoryReplacements = Map("aaa" -> "bbb"),
      textReplacements = Map("ccc" -> "ddd"),
      ignoreDirectoryNamePatterns = Seq("eee", "fff"),
      ignoreFileNamePatterns = Seq("ggg", "hhh")
    )
    val configuration = jsonConfiguration.toConfiguration
    assert(configuration.templateDirectory === Paths.get(jsonConfiguration.templateDirectory))
    assert(configuration.destinationDirectory === Paths.get(jsonConfiguration.destinationDirectory))
    assert(configuration.directoryReplacements === Map(Paths.get("aaa") -> Paths.get("bbb")))
    assert(configuration.textReplacements === jsonConfiguration.textReplacements)
    assert(configuration.ignoreDirectoryNamePatterns === jsonConfiguration.ignoreDirectoryNamePatterns)
    assert(configuration.ignoreFileNamePatterns === jsonConfiguration.ignoreFileNamePatterns)
  }
}
