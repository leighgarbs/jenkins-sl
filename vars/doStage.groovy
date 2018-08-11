#!groovy

def call(stageName, stageBody)
{
  stage (stageName)
  {
    gitlabCommitStatus(name: stageName)
    {
      stageBody()
    }

    if (currentBuild.result == 'UNSTABLE')
    {
      updateGitlabCommitStatus(name: stageName, state: 'failed')
    }
  }
}
