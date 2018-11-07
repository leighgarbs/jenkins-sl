#!groovy

def call()
{
  def script = libraryResource 'cleanUp'
  sh returnStatus: true, script: script
}
