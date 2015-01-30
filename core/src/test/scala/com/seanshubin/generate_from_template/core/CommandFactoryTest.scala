package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import org.scalatest.FunSuite

class CommandFactoryTest extends FunSuite {
  test("typical") {
    val templateDirectory = Paths.get("aaa", "bbb")
    val destinationDirectory = Paths.get("iii", "jjj", "kkk")
    val sourcePath1 = Paths.get("aaa", "bbb", "ccc", "ddd")
    val sourcePath2 = Paths.get("aaa", "bbb", "ccc", "eee")
    val sourcePath3 = Paths.get("aaa", "bbb", "fff", "ggg", "hhh")
    val sourcePaths = Seq(
      sourcePath1,
      sourcePath2,
      sourcePath3
    )
    val destinationPath1 = Paths.get("iii", "jjj", "kkk", "lll", "mmm", "ddd")
    val destinationPath2 = Paths.get("iii", "jjj", "kkk", "lll", "mmm", "eee")
    val destinationPath3 = Paths.get("iii", "jjj", "kkk", "nnn", "hhh")
    val directoryReplacements = Map(
      Paths.get("ccc") -> Paths.get("lll", "mmm"),
      Paths.get("fff", "ggg") -> Paths.get("nnn")
    )
    val expectedCommands: Seq[CopyFileCommand] = Seq(
      CopyFileCommand(sourcePath1, destinationPath1),
      CopyFileCommand(sourcePath2, destinationPath2),
      CopyFileCommand(sourcePath3, destinationPath3)
    )

    val commandFactory = new CommandFactoryImpl

    val actualCommands = commandFactory.generateCommands(
      sourcePaths, templateDirectory, destinationDirectory, directoryReplacements)

    val compareResult = SequenceComparison.compare(actualCommands, expectedCommands)

    assert(compareResult.areSame, compareResult.toMultipleLineString.mkString("\n"))
  }
}
