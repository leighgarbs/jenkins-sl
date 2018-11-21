#!groovy

def call(stageName, stageBody, stageArgs = [])
{
    stage (stageName)
    {
        timestamps
        {
            gitlabCommitStatus(name: stageName)
            {
                // Run the body of the stage with the given arguments
                def returnCode = stageBody(stageArgs)

                // The stage has been run.  Archive whatever artifacts the stage
                // created.  Note that this won't be reached if the stage throws
                // an exception.
                runResourceScript('saveArtifacts')

                // A non-zero stage return code or an unstable status fails the
                // build.  We just consider unstable builds failed; unstable
                // builds are just as unacceptable as failed builds.
                if (returnCode != 0 || currentBuild.result == 'UNSTABLE')
                {
                    currentBuild.result = 'FAILURE'
                }

                if (currentBuild.result == 'FAILURE')
                {
                    // Gitlab doesn't have a commit status for unstable
                    updateGitlabCommitStatus(name: stageName, state: 'failed')

                    // Stop the entire pipeline and report an error.  No sense
                    // proceeding when we know now that there is a problem to
                    // fix.  This might need to change if/when the pipeline
                    // becomes parallelized.
                    error("Stage \"" + stageName + "\" exited unsuccessfully")
                }

                return returnCode
            }
        }
    }
}
