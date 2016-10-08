package com.seanshubin.generate_from_template.console

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.devon.domain.DevonMarshallerWiring
import com.seanshubin.devon.parserules.DevonMarshaller
import com.seanshubin.generate_from_template.core._
import com.seanshubin.utility.filesystem.{FileSystemIntegration, FileSystemIntegrationImpl}

trait LauncherWiring {
  def commandLineArguments: Seq[String]

  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystemIntegration = new FileSystemIntegrationImpl
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val notifications: Notifications = new LineEmittingNotifications(devonMarshaller, emitLine)
  lazy val configurationFactory: ConfigurationFactory = new ConfigurationFactoryImpl(
    fileSystem, devonMarshaller, charset)
  lazy val createRunner: Configuration => Runnable = (theConfiguration) => new RunnerWiring {
    override def configuration: Configuration = theConfiguration
  }.runner
  lazy val launcher: Runnable = new LauncherImpl(
    commandLineArguments, configurationFactory, createRunner, notifications)
}
