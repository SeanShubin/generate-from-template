package com.seanshubin.generate_from_template.console

object ConsoleApplication extends App {
  new LauncherWiring {
    override def commandLineArguments: Seq[String] = args
  }.launcher.run()
}
