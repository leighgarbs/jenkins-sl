#!groovy

def call(stageName, stageBody, stageArgs = [])
{
  stage (stageName)
  {
    timestamps
    {
      gitlabCommitStatus(name: stageName)
      {
        try
        {
          if (stageBody(stageArgs) != 0)
          {
            currentBuild.result == 'FAILURE'
          }
        }
        catch (err)
        {
          // Any exception fails the build
          currentBuild.result = 'FAILURE'
        }
        finally
        {
          runResourceScript('saveArtifacts')
        }
      }

      if (currentBuild.result == 'UNSTABLE' ||
          currentBuild.result == 'FAILURE')
      {
        // Gitlab doesn't have a commit status for unstable
        updateGitlabCommitStatus(name: stageName, state: 'failed')

        // Stop the entire pipeline and report an error if the build is set to
        // FAILURE.  No sense proceeding when we know now there is a problem to
        // fix.  This might need to change if/when the pipeline becomes
        // parallelized.
        if (currentBuild.result == 'FAILURE')
        {
          error("Stage \"" + stageName + "\" exited unsuccessfully")
        }
      }
    }
  }
}
