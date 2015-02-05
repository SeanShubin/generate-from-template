package com.seanshubin.generate_from_template.console

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.generate_from_template.core._
import com.seanshubin.utility.file_system.{FileSystemIntegration, FileSystemIntegrationImpl}

trait ProductionRunnerWiring {
  def configuration: Configuration

  lazy val emitLine: String => Unit = println
  lazy val fileSystemIntegration: FileSystemIntegration = new FileSystemIntegrationImpl()
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val notifications: Notifications = new NotificationsImpl(emitLine)
  lazy val fileSystem: FileSystem = new FileSystemImpl(fileSystemIntegration, charset, notifications)
  lazy val commandFactory: CommandFactory = new CommandFactoryImpl
  lazy val commandExecutor: CommandExecutor = new CommandExecutorImpl(fileSystem, configuration.textReplacements)
  lazy val runner: Runner = new RunnerImpl(
    configuration.templateDirectory,
    configuration.destinationDirectory,
    configuration.directoryReplacements,
    fileSystem,
    commandFactory,
    commandExecutor,
    configuration.ignoreDirectoryNamePatterns,
    configuration.ignoreFileNamePatterns)
}

object ProductionRunnerWiring {
  def apply(theConfiguration: Configuration) = new ProductionRunnerWiring {
    override def configuration: Configuration = theConfiguration
  }
}
