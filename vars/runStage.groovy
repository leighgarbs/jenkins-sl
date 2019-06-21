#!groovy

// This could be a method of the Stage class but with a trivial implementation
// we lose access to the pipeline constructs like "stage" and "currentBuild"
// that way.  There may be a way around this.

def call(stageIn)
{
    def platformName = ''

    if (isUnix())
    {
        // MacOS will also cause isUnix() to return true, but we don't support
        // automated MacOS builds yet
        platformName = 'Linux'
    }
    else
    {
        // The only other platform we support automated builds for is Windows
        platformName = 'Windows'
    }

    // This is where the stage name that shows up in the Jenkins GUI pipeline
    // widget is actually set.  Stage names should be unique.  It is possible to
    // give multiple stages the same name but the Jenkins GUI pipeline widget
    // will bug out if this is done.
    stage (stageIn.name + ' (' + platformName + ')')
    {
        // We don't use only the return code from the stage to determine stage
        // success.  Jenkins tools like the Cppcheck publisher and Valgrind
        // publisher fail or unstable builds in a way that shows up in
        // "currentBuild.result".  They might do this if they're configured to
        // fail or unstable builds that were unacceptable to them in some way
        // (for example, too many static analysis issues detected).

        // If the stage body returns unsuccessfully then mark the build as
        // failed.  There isn't a return code for unstable, but at this point
        // currentBuild.result will be set to unstable if the stage is unstable.
        // By potentially setting the build result to failed here we may
        // override an unstable build result with a failed build result.  That
        // seems fine since we naturally want the build result to represent the
        // worst outcome.

        gitlabCommitStatus(name: stageName)
        {
            if (stageIn.body.call(stageIn.args) != 0)
            {
                // Gitlab doesn't have a commit status for unstable
                updateGitlabCommitStatus(name: stageName, state: 'failed')

                error('Stage ' + stageIn.name + ' failed on ' + platformName)
            }
        }
    }
}
