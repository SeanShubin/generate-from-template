package com.seanshubin.templater.console

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.devon.domain.{DevonMarshaller, DevonMarshallerWiring}
import com.seanshubin.templater.domain._

trait RunnerWiring {
  def configuration: Configuration

  lazy val emitLine: String => Unit = println
  lazy val files: FilesContract = FilesDelegate
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val notifications: Notifications = new LineEmittingNotifications(devonMarshaller, emitLine)
  lazy val fileSystem: FileSystem = new FileSystemImpl(files, charset, notifications)
  lazy val commandFactory: CommandFactory = new CommandFactoryImpl
  lazy val commandExecutor: CommandExecutor = new CommandExecutorImpl(fileSystem, configuration.textReplacements)
  lazy val runner: Runnable = new RunnerImpl(
    configuration.templateDirectory,
    configuration.destinationDirectory,
    configuration.directoryReplacements,
    fileSystem,
    commandFactory,
    commandExecutor,
    configuration.ignoreDirectoryNamePatterns,
    configuration.ignoreFileNamePatterns)
}
