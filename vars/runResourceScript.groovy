#!groovy

def call(scriptName)
{
  def script = libraryResource scriptName
  def returnCode = sh returnStatus: true, script: script
  return returnCode
}
