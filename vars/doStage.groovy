#!groovy

def call(name, body, args = [])
{
  stage (name)
  {
    gitlabCommitStatus(name: name)
    {
      try
      {
        body(args)
      }
      catch (err)
      {
        currentBuild.result = 'FAILURE'
      }
      finally
      {
        saveArtifacts()
      }
    }

    if (currentBuild.result == 'UNSTABLE' ||
        currentBuild.result == 'FAILURE')
    {
      updateGitlabCommitStatus(name: name, state: 'failed')
    }
  }
}
