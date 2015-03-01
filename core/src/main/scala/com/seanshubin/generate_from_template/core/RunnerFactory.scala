package com.seanshubin.generate_from_template.core

trait RunnerFactory {
  def createRunner(configuration: Configuration): Runner
}
