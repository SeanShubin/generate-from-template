package com.seanshubin.generate_from_template.core

case class Configuration(templateDirectory: String,
                         destinationDirectory: String,
                         directoryReplacements: Map[String, String],
                         textReplacements: Map[String, String])

/*
{
  templateDirectory     /home/sshubin/github/sean/template-scala-web
  destinationDirectory  /home/sshubin/github/sean/generate-from-template
  directoryReplacements {
    com/seanshubin/template/scala/console/ com/seanshubin/generate_from_template
  }
  textReplacements      {
    com.seanshubin.template.scala.console  com.seanshubin.generate_from_template
    template-scala-console                 generate-from-template
    com/seanshubin/template/scala/console/ com/seanshubin/generate_from_template/
  }
}
*/