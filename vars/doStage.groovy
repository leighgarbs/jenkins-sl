#!groovy

def call(name, body, args = [])
{
  stage (name)
  {
    timestamps
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

        if (currentBuild.result == 'FAILURE')
        {
          error('Build result is FAILURE')
        }
      }
    }
  }
}
