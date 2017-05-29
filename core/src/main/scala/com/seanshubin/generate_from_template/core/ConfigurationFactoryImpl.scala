package com.seanshubin.generate_from_template.core

import java.nio.charset.Charset
import java.nio.file.Paths

import com.seanshubin.devon.domain.DevonMarshaller

class ConfigurationFactoryImpl(files: FilesContract,
                               devonMarshaller: DevonMarshaller,
                               charset: Charset) extends ConfigurationFactory {
  private val sampleConfiguration: Configuration = Configuration(
    templateDirectory = Paths.get("..", "template-scala-web"),
    destinationDirectory = Paths.get("..", "..", "generated", "foo-bar"),
    directoryReplacements = Map(
      Paths.get("template", "scala", "web") -> Paths.get("foo", "bar")
    ),
    textReplacements = Map(
      "template.scala.web" -> "foo.bar",
      "template-scala-web" -> "foo-bar",
      "template/scala/web" -> "foo/bar"
    ),
    ignoreDirectoryNamePatterns = Seq("target", "\\.git", "\\.idea"),
    ignoreFileNamePatterns = Seq("\\.gitattributes", ".*\\.iml")
  )

  override def validate(args: Seq[String]): Either[Seq[String], Configuration] = {
    if (args.length == 1) {
      val configFilePath = Paths.get(args(0))
      try {
        if (files.exists(configFilePath)) {
          val bytes = files.readAllBytes(configFilePath)
          val text = new String(bytes, charset)
          val devon = devonMarshaller.fromString(text)
          val config = devonMarshaller.toValue(devon, classOf[Configuration])
          Right(config)
        } else {
          Left(Seq(s"Configuration file named '$configFilePath' not found"))
        }
      } catch {
        case ex: Throwable =>
          Left(Seq(s"There was a problem reading the configuration file '$configFilePath': ${ex.getMessage}"))
      }
    } else {
      val sampleConfigDevon = devonMarshaller.fromValue(sampleConfiguration)
      val prettySampleLines = devonMarshaller.toPretty(sampleConfigDevon)
      Left(Seq(
        "Expected exactly one argument, the name of the configuration file",
        "A typical configuration file might look something like this",
        "") ++ prettySampleLines)
    }
  }
}
