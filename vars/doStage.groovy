#!groovy

def call(stageName, stageBody, stageArgs = [])
{
  stage (stageName)
  {
    gitlabCommitStatus(name: stageName)
    {
      try
      {
        stageBody(stageArgs)
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
      updateGitlabCommitStatus(name: stageName, state: 'failed')

      if (currentBuild.result == 'FAILURE')
      {
        error
      }
    }
  }
}
