package com.seanshubin.generate_from_template.core

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Paths

import com.seanshubin.devon.core.devon.{DefaultDevonMarshaller, DevonMarshaller}
import com.seanshubin.utility.file_system.FileSystemIntegration
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ConfigurationFactoryImplTest extends FunSuite with EasyMockSugar {
  private val completeConfiguration: Configuration = Configuration(
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

  test("complete configuration") {
    new Helper {
      override def content =
        """{
          |  templateDirectory ../template-scala-web
          |  destinationDirectory ../../generated/foo-bar
          |  directoryReplacements
          |  {
          |    template/scala/web foo/bar
          |  }
          |  textReplacements
          |  {
          |    template.scala.web foo.bar
          |    template-scala-web foo-bar
          |    template/scala/web foo/bar
          |  }
          |  ignoreDirectoryNamePatterns
          |  [
          |    target
          |    \.git
          |    \.idea
          |  ]
          |  ignoreFileNamePatterns
          |  [
          |    \.gitattributes
          |    .*\.iml
          |  ]
          |}
          | """.stripMargin

      override def expected = Right(completeConfiguration)

      override def expecting = () => {
        mockFileSystem.exists(configFilePath).andReturn(true)
        mockFileSystem.readAllBytes(configFilePath).andReturn(contentBytes)
      }

      override def whenExecuting = () => {
        val actual = configurationFactory.validate(args)
        assert(actual === expected)
      }
    }
  }

  test("missing configuration file") {
    new Helper {
      override def content =
        """{
          |  servePathOverride gui/src/main/resources/
          |  optionalPathPrefix /template
          |}
          | """.stripMargin

      override def expected = Left(Seq("Configuration file named 'environment.txt' not found"))

      override def expecting = () => {
        mockFileSystem.exists(configFilePath).andReturn(false)
      }

      override def whenExecuting = () => {
        val actual = configurationFactory.validate(args)
        assert(actual === expected)
      }
    }
  }

  test("malformed configuration") {
    new Helper {
      override def content = "{"

      override def expected = Left(Seq("There was a problem reading the configuration file 'environment.txt': Could not match 'element', expected one of: map, array, string, null"))

      override def expecting = () => {
        mockFileSystem.exists(configFilePath).andReturn(true)
        mockFileSystem.readAllBytes(configFilePath).andReturn(contentBytes)
      }

      override def whenExecuting = () => {
        val actual = configurationFactory.validate(args)
        assert(actual === expected)
      }
    }
  }

  trait Helper {
    def content: String

    def expected: Either[Seq[String], Configuration]

    val configFileName: String = "environment.txt"
    val args = Seq(configFileName)
    val mockFileSystem: FileSystemIntegration = mock[FileSystemIntegration]
    val devonMarshaller: DevonMarshaller = DefaultDevonMarshaller
    val charset: Charset = StandardCharsets.UTF_8
    val configurationFactory = new ConfigurationFactoryImpl(mockFileSystem, devonMarshaller, charset)
    val configFilePath = Paths.get(configFileName)
    val contentBytes = content.getBytes(charset)
    val mocks = Array(mockFileSystem)

    def expecting: () => Unit

    def whenExecuting: () => Unit

    expecting()
    EasyMockSugar.whenExecuting(mockFileSystem) {
      whenExecuting()
    }
  }

}
