package com.seanshubin.templater.domain

trait CommandExecutor {
  def execute(command: CopyFileCommand)
}
