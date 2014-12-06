package com.seanshubin.generate_from_template.console

import com.seanshubin.generate_from_template.core.{Launcher, LauncherImpl, Runner}

trait ProductionLauncherWiring {
  def commandLineArguments: Seq[String]

  lazy val createRunner: String => Runner =
    target => ProductionRunnerWiring(target).runner

  lazy val launcher: Launcher = new LauncherImpl(commandLineArguments, createRunner)

}

object ProductionLauncherWiring {
  def apply(theCommandLineArguments: Seq[String]) = new ProductionLauncherWiring {
    override def commandLineArguments: Seq[String] = theCommandLineArguments
  }
}
