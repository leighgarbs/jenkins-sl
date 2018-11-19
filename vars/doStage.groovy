#!groovy

def call(stageName, stageBody, stageArgs = [])
{
  stage (stageName)
  {
    timestamps
    {
      def returnCode = 0

      gitlabCommitStatus(name: stageName)
      {
        try
        {
          returnCode = stageBody(stageArgs)
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

        // A non-zero stage return code fails the build
        if (returnCode != 0)
        {
          currentBuild.result = 'FAILURE'
        }
      }

      if (currentBuild.result == 'UNSTABLE' || currentBuild.result == 'FAILURE')
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

      return returnCode
    }
  }
}
