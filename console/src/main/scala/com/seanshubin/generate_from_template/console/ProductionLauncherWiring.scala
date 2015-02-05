package com.seanshubin.generate_from_template.console

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.generate_from_template.core._
import com.seanshubin.utility.file_system.{FileSystemIntegration, FileSystemIntegrationImpl}
import com.seanshubin.utility.json.{JsonMarshaller, JsonMarshallerImpl}

trait ProductionLauncherWiring {
  def commandLineArguments: Seq[String]

  lazy val createRunner: Configuration => Runner =
    configuration => ProductionRunnerWiring(configuration).runner

  lazy val fileSystemIntegration: FileSystemIntegration = new FileSystemIntegrationImpl
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val emitLine: String => Unit = println
  lazy val notifications: Notifications = new NotificationsImpl(emitLine)
  lazy val fileSystem: FileSystem = new FileSystemImpl(fileSystemIntegration, charset, notifications)
  lazy val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl
  lazy val launcher: Launcher = new LauncherImpl(commandLineArguments, fileSystem, jsonMarshaller, createRunner)

}

object ProductionLauncherWiring {
  def apply(theCommandLineArguments: Seq[String]) = new ProductionLauncherWiring {
    override def commandLineArguments: Seq[String] = theCommandLineArguments
  }
}
