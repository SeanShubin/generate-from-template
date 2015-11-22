package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class LauncherImplTest extends FunSuite {
  val validConfiguration: Configuration = Configuration(
    templateDirectory = Paths.get("..", "template-scala-web"),
    destinationDirectory = Paths.get("..", "..", "generated", "foo-bar"),
    directoryReplacements = Map(
      Paths.get("template", "scala", "web") -> Paths.get("foo", "bar")
    ),
    textReplacements = Map(
      "template.scala.web" -> "foo.bar",
      "template-scala-web" -> "foo-bar",
      "template/scala/web" -> "foo/bar"
    ),
    ignoreDirectoryNamePatterns = Seq("target", "\\.git", "\\.idea"),
    ignoreFileNamePatterns = Seq("\\.gitattributes", ".*\\.iml")
  )

  test("valid configuration") {
    val helper = new Helper(validationResult = Right(validConfiguration))
    helper.launcher.run()
    assert(helper.sideEffects.size === 2)
    assert(helper.sideEffects(0) ===("notifications.effectiveConfiguration", validConfiguration))
    assert(helper.sideEffects(1) ===("runner.run", ()))

  }

  test("invalid configuration") {
    val helper = new Helper(validationResult = Left(Seq("error")))
    helper.launcher.run()
    assert(helper.sideEffects.size === 1)
    assert(helper.sideEffects(0) ===("notifications.configurationError", Seq("error")))
  }

  class Helper(validationResult: Either[Seq[String], Configuration]) {
    val sideEffects: ArrayBuffer[(String, Any)] = new ArrayBuffer()
    val configurationFactory = new FakeConfigurationFactory(Seq("foo.txt"), validationResult)
    val runner = new FakeRunner(sideEffects)
    val runnerFactory: Configuration => Runnable = (theConfiguration) => runner
    val notifications = new FakeNotifications(sideEffects)
    val launcher = new LauncherImpl(Seq("foo.txt"), configurationFactory, runnerFactory, notifications)
  }

  class FakeConfigurationFactory(expectedArgs: Seq[String], result: Either[Seq[String], Configuration]) extends ConfigurationFactory {
    override def validate(args: Seq[String]): Either[Seq[String], Configuration] = {
      assert(args === expectedArgs)
      result
    }
  }

  class FakeRunner(sideEffects: ArrayBuffer[(String, Any)]) extends Runnable {
    override def run(): Unit = sideEffects.append(("runner.run", ()))
  }

}
