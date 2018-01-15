package com.seanshubin.templater.console

object ConsoleApplication extends App {
  new LauncherWiring {
    override def commandLineArguments: Seq[String] = args
  }.launcher.run()
}
