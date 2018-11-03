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
          // Any exception fails the build
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
        // Gitlab doesn't have a commit status for unstable
        updateGitlabCommitStatus(name: name, state: 'failed')

        // Stop the entire pipeline and report an error if the build is set to
        // FAILURE.  No sense proceeding when we know now there is a problem to
        // fix.  This might need to change if/when the pipeline becomes
        // parallelized.
        if (currentBuild.result == 'FAILURE')
        {
          error('Build result is FAILURE')
        }
      }
    }
  }
}
