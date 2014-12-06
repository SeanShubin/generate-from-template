package com.seanshubin.generate_from_template.core

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class LauncherTest extends FunSuite with EasyMockSugar {
  test("launch") {
    val commandLineArguments = Seq("some-configuration-file")
    val createRunner = mock[String => Runner]
    val runner = mock[Runner]
    val launcher = new LauncherImpl(commandLineArguments, createRunner)

    expecting {
      createRunner("some-configuration-file").andReturn(runner)
      runner.run()
    }

    whenExecuting(createRunner, runner) {
      launcher.launch()
    }
  }
}
