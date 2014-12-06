package com.seanshubin.generate_from_template.console

import com.seanshubin.generate_from_template.core.{Runner, RunnerImpl}

trait ProductionRunnerWiring {
  def configuration: String

  lazy val emitLine: String => Unit = println
  lazy val runner: Runner = new RunnerImpl(configuration, emitLine)
}

object ProductionRunnerWiring {
  def apply(theConfiguration: String) = new ProductionRunnerWiring {
    override def configuration: String = theConfiguration
  }
}
