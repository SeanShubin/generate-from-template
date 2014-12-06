package com.seanshubin.generate_from_template.core

class LauncherImpl(commandLineArguments: Seq[String], createRunner: String => Runner) extends Launcher {
  override def launch(): Unit = {
    val runner = createRunner(commandLineArguments(0))
    runner.run()
  }
}
