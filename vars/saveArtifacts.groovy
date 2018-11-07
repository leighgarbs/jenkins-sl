#!groovy

def call()
{
  def script = libraryResource 'saveArtifacts'
  sh returnStatus: true, script: script
}
