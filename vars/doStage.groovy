#!groovy

def call(stageName, stageBody, stageArgs = [])
{
  echo stageBody.getClass().getSuperclass().toString()
  stage (stageName)
  {
    gitlabCommitStatus(name: stageName)
    {
      stageBody(stageArgs)
    }

    if (currentBuild.result == 'UNSTABLE')
    {
      updateGitlabCommitStatus(name: stageName, state: 'failed')
    }
  }
}
