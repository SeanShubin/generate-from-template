package com.seanshubin.generate_from_template.console

import com.seanshubin.generate_from_template.core.{Configuration, Runner, RunnerFactory}

class RunnerFactoryImpl extends RunnerFactory {
  override def createRunner(theConfiguration: Configuration): Runner = {
    new RunnerWiring {
      override def configuration: Configuration = theConfiguration
    }.runner
  }
}
