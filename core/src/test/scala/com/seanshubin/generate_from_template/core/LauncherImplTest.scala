package com.seanshubin.generate_from_template.core

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class LauncherImplTest extends FunSuite with EasyMockSugar {
  test("valid configuration") {
    new Helper {
      override def expecting = () => {
        configurationFactory.validate(args).andReturn(validationSuccess)
        notifications.effectiveConfiguration(validConfiguration)
        runnerFactory.createRunner(validConfiguration).andReturn(runner)
        runner.run()
      }

      override def whenExecuting = () => {
        launcher.launch()
      }
    }
  }

  test("invalid configuration") {
    new Helper {
      override def expecting = () => {
        configurationFactory.validate(args).andReturn(validationFailure)
        notifications.configurationError(errorLines)
      }

      override def whenExecuting = () => {
        launcher.launch()
      }
    }
  }

  trait Helper {
    val args: Seq[String] = Seq("arg1")
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

    val validationSuccess = Right(validConfiguration)
    val errorLines = Seq("error")
    val validationFailure = Left(errorLines)
    val configurationFactory = mock[ConfigurationFactory]
    val runnerFactory = mock[RunnerFactory]
    val notifications = mock[Notifications]
    val runner = mock[Runner]
    val launcher = new LauncherImpl(args, configurationFactory, runnerFactory, notifications)

    def expecting: () => Unit

    def whenExecuting: () => Unit

    expecting()
    EasyMockSugar.whenExecuting(configurationFactory, runnerFactory, notifications, runner) {
      whenExecuting()
    }
  }

}
