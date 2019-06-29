#!groovy

def call(scriptName)
{
  def script = libraryResource scriptName
  sh returnStatus: true, script: script
}
